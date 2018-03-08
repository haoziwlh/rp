package com.jds.rp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Verify;
import com.jds.rp.ServerConfig;
import com.jds.rp.dao.RedPacketDao;
import com.jds.rp.dao.RedPacketRecieveDao;
import com.jds.rp.dao.UserDao;
import com.jds.rp.dao.dto.RedPacket;
import com.jds.rp.dao.dto.RedPacketRecieve;
import com.jds.rp.dao.dto.User;
import com.jds.rp.enums.EnumWX;
import com.jds.rp.service.vo.RedPacketRecieveVo;
import com.jds.rp.service.vo.RedPacketVo;
import com.jds.rp.service.vo.RpVo;
import com.jds.rp.service.vo.SessionWx;

@Service
public class UserService {
	private static final String KEYPREFIX = "rp:packets:";
	@Autowired
	private HttpService httpservice;
	
	@Autowired
	private ServerConfig config;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RedPacketDao redPacketDao;
	
	@Autowired
	private RedisTemplate<String, Object> redis;
	
	@Autowired
	private RedPacketRecieveDao redPacketRecieveDao;
	
	public int bind(String jsCode, String nickName, String avatarUrl) {
		Map<String, Object> map = new HashMap<>();
		map.put("secret", config.getSecret());
		map.put("appid", config.getAppid());
		map.put("js_code", jsCode);
		map.put("grant_type", config.getGrantType());
		String retSession = httpservice.getWithParams(EnumWX.CODE_SESSION.parsedUrl(), map);
		SessionWx sessionWx = JSONObject.parseObject(retSession, SessionWx.class);
		Verify.verifyNotNull(sessionWx.getOpenid(), "openid get failed");
		
		
		
		User u = userDao.getUser(sessionWx.getOpenid());
		if(u != null) {
			return u.getId();
		}else {
			User user = new User();
			user.setOpenid(sessionWx.getOpenid());
			user.setImg(avatarUrl);
			user.setNickName(nickName);
			return userDao.insert(user);
		}
	}
	
	public User balance(int id) {
		return userDao.getUser(id);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int sendRp(int id, int amt, String memo, int type, int num) {
		int rpId = 0;
		User user = userDao.getUser(id);
		if(user == null) {
			return rpId;
		}
		
		int amtStored = user.getAmt();
		if(amt > amtStored) {
			return rpId;
		}
		
		int lockAmt = userDao.lockAmt(id, amt);
		if(lockAmt <= 0) {
			return rpId;
		}
		
		
		RedPacket rp = new RedPacket();
		rp.setAmt(amt);
		rp.setCurBal(amt);
		rp.setMemo(memo);
		rp.setNum(num);
		rp.setType(type);
		rp.setUid(id);
		int rid = redPacketDao.insert(rp);
		
		redis.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				if(type == 1) {
					int value = amt / num;
					for(int i = 0;i < num; i++) {
						int valueCopy = value;
						if(i == num - 1) {
							valueCopy = amt - i * value;
						}
						connection.lPush((KEYPREFIX + rid).getBytes(), String.valueOf(valueCopy).getBytes());
					}
				}else if(type == 2) {
					int numCopy = num;
					int amtCopy = amt;
					Random random = new Random();
					while(numCopy > 1) {
						int range = amtCopy - (numCopy - 1);
						int value = 1; 
						if(range > 1) {
							value = random.nextInt(range) + 1;
						}
						connection.lPush((KEYPREFIX + rid).getBytes(), String.valueOf(value).getBytes());
						amtCopy -=  value;
						numCopy -= 1;
					}
				}
				return true;
			}
		});
		
		return rid;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public RedPacketRecieveVo getRp(int uid, int rid) {
		RedPacketRecieveVo vo = new RedPacketRecieveVo();
		User user = userDao.getUser(uid);
		if(user == null) {
			vo.setGet(false);
			vo.setError("uid not exists");
			return vo;
		}
		
		RedPacketVo redPacket = redPacketDao.getDetail(rid);
		if(redPacket == null) {
			vo.setGet(false);
			vo.setError("rid not exists");
			return vo;
		}
		
		int amt = redPacket.getAmt();
		int curBal = redPacket.getCurBal();
		int num = redPacket.getNum();
		int numRec = redPacket.getNumRec();
		
		vo.setType(redPacket.getType());
		vo.setNum(num);
		vo.setNumRec(numRec);
		vo.setAmt(amt);
		vo.setUid(redPacket.getUid());
		vo.setNickName(redPacket.getNickName());
		vo.setImg(redPacket.getImg());
		
		//同一个用户
//		if(redPacket.getUid() == uid) {
//			vo.setGet(false);
//			return vo;
//		}
		
		//领过
		int count = redPacketRecieveDao.get(rid, uid);
		if(count > 0) {
			vo.setGet(false);
			return vo;
		}
		
		//领完
		if(curBal <= 0 || numRec >= num) {
			return vo;
		}
		
		return redis.execute(new RedisCallback<RedPacketRecieveVo>() {

			@Override
			public RedPacketRecieveVo doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] num = connection.lPop((KEYPREFIX + rid).getBytes());
				int rpv = Integer.parseInt(new String(num));
				int update = redPacketDao.update(rid, rpv);
				if(update != 1) {
					return vo;
				}
				
				RedPacketRecieve rpr = new RedPacketRecieve();
				rpr.setRid(rid);
				rpr.setUid(uid);
				rpr.setNum(rpv);
				int insert = redPacketRecieveDao.insert(rpr);
				if(insert != 1) {
					vo.setNumRec(numRec + 1);
				}
				
				userDao.unlockAmt(redPacket.getUid(), rpv);
				userDao.addAmt(uid, rpv);
				
				vo.setGet(true);
				return vo;
			}
			
		});
		
		
	}
	
	public List<RpVo> list(int rid) {
		return redPacketRecieveDao.listRecords(rid);
	}
	
}

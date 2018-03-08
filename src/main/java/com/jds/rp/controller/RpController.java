package com.jds.rp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import com.jds.rp.dao.dto.User;
import com.jds.rp.service.UserService;
import com.jds.rp.service.vo.Bind;
import com.jds.rp.service.vo.RedPacketRecieveVo;
import com.jds.rp.service.vo.Ret;
import com.jds.rp.service.vo.RetRedPacket;
import com.jds.rp.service.vo.RpVo;

@Controller
@RequestMapping("/rp")
public class RpController {
	private static final Logger logger = LoggerFactory.getLogger(RpController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/api/1/hello", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	String hello() {
		return "hello";
	}
	
	@RequestMapping(value = "/api/1/bind", method={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	String bind(HttpServletRequest request) {
		Ret ret = Ret.buildOk();
		
		String jsCode = ServletRequestUtils.getStringParameter(request, "code", null);
		String nickName = ServletRequestUtils.getStringParameter(request, "nickName", "");
		String avatarUrl = ServletRequestUtils.getStringParameter(request, "avatarUrl", "");
		logger.info("recv bind > code {}, nickName {}, avatarUrl {}", jsCode, nickName, avatarUrl);
		try {
			Verify.verifyNotNull(jsCode, "code is null");
			int bind = userService.bind(jsCode, nickName, avatarUrl);
			if(bind <= 0) {
				ret = Ret.buildError("绑定失败");
			}
			Bind b = new Bind();
			b.setId(bind);
			ret.setData(b);
		} catch (VerifyException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			ret = Ret.buildError(e.getMessage());
		}
		return JSONObject.toJSONString(ret);
	}
	
	@RequestMapping("/api/1/balance")
	@ResponseBody
	String balance(HttpServletRequest request) {
		Ret ret = Ret.buildOk();
		
		String id = ServletRequestUtils.getStringParameter(request, "uid", null);
		logger.info("recv balance > uid {}", id);
		
		try {
			Verify.verifyNotNull(id, "id is null");
			User user = userService.balance(Integer.parseInt(id));
			if(user == null) {
				ret = Ret.buildError("请先绑定");
			}else {
				user.setOpenid(null);
				ret.setData(user);
			}
			
		} catch (VerifyException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			ret = Ret.buildError(e.getMessage());
		}
		return JSONObject.toJSONString(ret);
	}
	
	@RequestMapping("/api/1/sendrp")
	@ResponseBody
	String sendRp(HttpServletRequest request) {
		Ret ret = Ret.buildOk();
		
		int uid = ServletRequestUtils.getIntParameter(request, "uid", 0);
		int amt = ServletRequestUtils.getIntParameter(request, "amt", 0);
		int num = ServletRequestUtils.getIntParameter(request, "num", 0);
		int type = ServletRequestUtils.getIntParameter(request, "type", 1);
		String memo = ServletRequestUtils.getStringParameter(request, "memo", null);
		logger.info("recv sendRp > uid {}, amt {}, num {}, type {}, memo {}", uid, amt, num, type, memo);
		try {
			Verify.verify(uid > 0, "uid is not allowed");
			int rpId = userService.sendRp(uid, amt, memo, type, num);
			if(rpId == 0) {
				ret = Ret.buildError("红包发送失败");
			}else {
				RetRedPacket retRedPacket = new RetRedPacket();
				retRedPacket.setId(rpId);
				ret.setData(retRedPacket);
			}
			
		} catch (VerifyException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			ret = Ret.buildError(e.getMessage());
		}
		return JSONObject.toJSONString(ret);
	}
	
	@RequestMapping("/api/1/getrp")
	@ResponseBody
	String getRp(HttpServletRequest request) {
		Ret ret = Ret.buildOk();
		
		int uid = ServletRequestUtils.getIntParameter(request, "uid", 0);
		int rid = ServletRequestUtils.getIntParameter(request, "rid", 0);
		logger.info("recv getrp > uid {}, rid {}", uid, rid);
		try {
			Verify.verify(uid > 0, "uid is not allowed");
			RedPacketRecieveVo redPacketRecieveVo = userService.getRp(uid, rid);
			ret.setData(redPacketRecieveVo);
			
		} catch (VerifyException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			ret = Ret.buildError(e.getMessage());
		}
		return JSONObject.toJSONString(ret);
	}
	
	@RequestMapping("/api/1/getrplist")
	@ResponseBody
	String getRpList(HttpServletRequest request) {
		Ret ret = Ret.buildOk();
		
		int rid = ServletRequestUtils.getIntParameter(request, "rid", 0);
		logger.info("recv getRpList > rid {}", rid);
		
		try {
			Verify.verify(rid > 0, "rid is not allowed");
			List<RpVo> vos = userService.list(rid);
			ret.setData(vos);
			
		} catch (VerifyException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			ret = Ret.buildError(e.getMessage());
		}
		return JSONObject.toJSONString(ret);
	}
}

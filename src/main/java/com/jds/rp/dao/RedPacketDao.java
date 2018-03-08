package com.jds.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jds.rp.dao.dto.RedPacket;
import com.jds.rp.service.vo.RedPacketVo;

@Repository
public class RedPacketDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int insert(RedPacket rp) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into red_packet(amt,cur_bal,num,type,uid,memo,ctime) values(?,?,?,?,?,?,now())";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, rp.getAmt());
				ps.setInt(2, rp.getCurBal());
				ps.setInt(3, rp.getNum());
				ps.setInt(4, rp.getType());
				ps.setInt(5, rp.getUid());
				ps.setString(6, rp.getMemo());
				return ps;
			}

		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public RedPacket get(int id) {
		try {
			return jdbcTemplate.queryForObject("select * from red_packet where id=?", new Object[]{id}, new RedPacketMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public RedPacketVo getDetail(int id) {
		try {
			String sql = "select rp.id as id,rp.amt,rp.cur_bal,rp.num,rp.num_rec,rp.type,rp.uid,rp.memo,rp.ctime,u.nick_name,u.img from red_packet rp left join user u on rp.uid = u.id where rp.id=?";
			return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RedPacketVoMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int update(int id, int num) {
		return jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "update red_packet set cur_bal=cur_bal-?,num_rec=num_rec+1 where id = ?";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, num);
				ps.setInt(2, id);
				return ps;
			}

		});
	}
	
	class RedPacketMapper implements RowMapper<RedPacket> {

		@Override
		public RedPacket mapRow(ResultSet rs, int rowNum) throws SQLException {
			RedPacket rp = new RedPacket();
			rp.setAmt(rs.getInt("amt"));
			rp.setId(rs.getInt("id"));
			rp.setCurBal(rs.getInt("cur_bal"));
			rp.setNum(rs.getInt("num"));
			rp.setNumRec(rs.getInt("num_rec"));
			rp.setType(rs.getInt("type"));
			rp.setUid(rs.getInt("uid"));
			rp.setMemo(rs.getString("memo"));
			rp.setCtime(rs.getDate("ctime"));
			return rp;
		}
		
	}
	
	class RedPacketVoMapper implements RowMapper<RedPacketVo> {

		@Override
		public RedPacketVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RedPacketVo rp = new RedPacketVo();
			rp.setAmt(rs.getInt("amt"));
			rp.setId(rs.getInt("id"));
			rp.setCurBal(rs.getInt("cur_bal"));
			rp.setNum(rs.getInt("num"));
			rp.setNumRec(rs.getInt("num_rec"));
			rp.setType(rs.getInt("type"));
			rp.setUid(rs.getInt("uid"));
			rp.setMemo(rs.getString("memo"));
			rp.setCtime(rs.getDate("ctime"));
			rp.setImg(rs.getString("img"));
			rp.setNickName(rs.getString("nick_name"));
			return rp;
		}
		
	}
}

package com.jds.rp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jds.rp.dao.dto.RedPacketRecieve;
import com.jds.rp.service.vo.RpVo;

@Repository
public class RedPacketRecieveDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int insert(RedPacketRecieve rpr) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into red_packet_recieve(uid,rid,num,time) values(?,?,?,now())";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, rpr.getUid());
				ps.setInt(2, rpr.getRid());
				ps.setInt(3, rpr.getNum());
				return ps;
			}

		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public List<RpVo> listRecords(int rid) {
		return jdbcTemplate.query("select uid,num,img,nick_name,time from (select * from red_packet_recieve where rid=?) as rpr left join user as u on rpr.uid=u.id", new Object[]{rid}, new RpVoMapper());
	}
	
	public int get(int rid, int uid) {
		return jdbcTemplate.queryForObject("select count(*) from red_packet_recieve where uid=? and rid=?", new Object[]{uid,rid}, Integer.class);
	}
	
	class RpVoMapper implements RowMapper<RpVo> {

		@Override
		public RpVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RpVo rpr = new RpVo();
			rpr.setUid(rs.getInt("uid"));
			rpr.setNum(rs.getInt("num"));
			rpr.setImg(rs.getString("img"));
			rpr.setNickName(rs.getString("nick_name"));
			Instant instant = rs.getTimestamp("time").toInstant();
			String time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
			rpr.setTime(time);
			return rpr;
		}
		
	}
}

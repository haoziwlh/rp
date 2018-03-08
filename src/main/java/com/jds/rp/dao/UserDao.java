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

import com.jds.rp.dao.dto.User;

@Repository
public class UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(User user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into user(openid,amt,frozen_amt,nick_name,img,bind_time) values(?,?,?,?,?,now())";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getOpenid());
				ps.setInt(2, user.getAmt());
				ps.setInt(3, user.getFrozenAmt());
				ps.setString(4, user.getNickName());
				ps.setString(5, user.getImg());
				return ps;
			}

		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public int get(String openid) {
		return jdbcTemplate.queryForObject("select count(*) from user where openid=?", new Object[]{openid}, Integer.class);
	}
	
	public User getUser(String openid) {
		try {
			return jdbcTemplate.queryForObject("select * from user where openid=?", new Object[]{openid}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public User getUser(int id) {
		try {
			return jdbcTemplate.queryForObject("select * from user where id=?", new Object[]{id}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int lockAmt(int id, int amt) {
		return jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "update user set amt=amt - ?,frozen_amt=? where id=?";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, amt);
				ps.setInt(2, amt);
				ps.setInt(3, id);
				return ps;
			}

		});
	}
	
	public int unlockAmt(int id, int amt) {
		return jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "update user set frozen_amt=frozen_amt-? where id=?";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, amt);
				ps.setInt(2, id);
				return ps;
			}

		});
	}
	
	public int addAmt(int id, int amt) {
		return jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "update user set amt=amt+? where id=?";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, amt);
				ps.setInt(2, id);
				return ps;
			}

		});
	}
	
	class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setAmt(rs.getInt("amt"));
			user.setBindTime(rs.getDate("bind_time"));
			user.setFrozenAmt(rs.getInt("frozen_amt"));
			user.setImg(rs.getString("img"));
			user.setNickName(rs.getString("nick_name"));
			user.setOpenid(rs.getString("openid"));
			return user;
		}
		
	}
}

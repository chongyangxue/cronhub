package org.cronhub.managesystem.auth.dao.impl;

import java.util.List;

import org.cronhub.managesystem.auth.dao.UserDao;
import org.cronhub.managesystem.commons.dao.BaseRowMapper;
import org.cronhub.managesystem.commons.dao.bean.AuthUser;
import org.cronhub.managesystem.commons.dao.bean.AuthLoginLog;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public AuthUser getById(String userId) {
		final String sql = "SELECT user_id,user_name,user_type,department,phone " +
				"FROM auth_user WHERE user_id = ?";
		AuthUser user = null;
		try {
			user = (AuthUser)this.jdbcTemplate.queryForObject(sql, 
				new Object[]{userId},new BaseRowMapper(AuthUser.class));
		}catch(DataAccessException e) {
		}
		return user;
	}

	@Override
	public boolean insert(AuthUser authUser) {
		final String insertSql = "INSERT INTO auth_user(user_id, user_name, " +
				"user_type, department, phone) VALUES(?,?,?,?,?)";
		try {
			this.jdbcTemplate.update(insertSql,
				new Object[]{
					authUser.getUser_id(), 
					authUser.getUser_name(), 
					authUser.getUser_type(),
					authUser.getDepartment(),
					authUser.getPhone()
				});
		}catch(DuplicateKeyException e) {
			return false;
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AuthUser> findAll() {
		final String sql = "SELECT user_id, user_name, user_type, department, phone FROM auth_user";
		List<AuthUser> userList = this.jdbcTemplate.query(sql, new BaseRowMapper(AuthUser.class));
		return userList;
	}

	@Override
	public boolean deleteById(String user_id) {
		final String sql = "DELETE FROM auth_user WHERE user_id = '" + user_id + "'";
		try {
			this.jdbcTemplate.update(sql);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void saveLoginLog(AuthLoginLog log) {
		final String sql = "INSERT INTO login_log(user_id, login_time)" +
				" VALUES(?,?)";
		this.jdbcTemplate.update(sql, new Object[]{log.getUser_id(), log.getLogin_time()});
	}

	@Override
	public void saveLogoutLog(AuthLoginLog log) {
		final String sql = "UPDATE login_log SET logout_time = ? WHERE user_id = ? AND logout_time is null";
		this.jdbcTemplate.update(sql, new Object[]{log.getLogout_time(), log.getUser_id()});
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AuthLoginLog> findAllLoginLog() {
		final String sql = "SELECT * from login_log order by login_time desc ";
		List<AuthLoginLog> loglist = jdbcTemplate.query(sql, new BaseRowMapper(AuthLoginLog.class));
		return loglist;
	}
}

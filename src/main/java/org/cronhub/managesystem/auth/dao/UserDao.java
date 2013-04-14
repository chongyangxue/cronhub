package org.cronhub.managesystem.auth.dao;

import java.util.List;

import org.cronhub.managesystem.commons.dao.bean.AuthUser;
import org.cronhub.managesystem.commons.dao.bean.AuthLoginLog;

public interface UserDao {

	AuthUser getById(String userId);

	boolean insert(AuthUser authUser);

	List<AuthUser> findAll();

	boolean deleteById(String user_id);

	void saveLoginLog(AuthLoginLog log);

	void saveLogoutLog(AuthLoginLog log);

	List<AuthLoginLog> findAllLoginLog();
}

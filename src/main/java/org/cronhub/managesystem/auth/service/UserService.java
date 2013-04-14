package org.cronhub.managesystem.auth.service;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.cronhub.managesystem.auth.dao.UserDao;
import org.cronhub.managesystem.auth.dao.impl.UserDaoImpl;
import org.cronhub.managesystem.commons.dao.bean.AuthUser;
import org.cronhub.managesystem.commons.dao.bean.AuthLoginLog;
import org.springframework.util.StringUtils;

public class UserService {
	private final static String LDAP_URL = "ldap://10.11.156.63:389/"; 
	private final static String LDAP_SUFFIX = "@sohu-inc.com";
	private UserDao userDao;
	
	public boolean ldapCheck(String userName, String password) {
		if(StringUtils.hasText(userName)) {
			if(!userName.endsWith(LDAP_SUFFIX)) {
				userName += LDAP_SUFFIX;
			}
			
			DirContext ctx = null;
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, LDAP_URL);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, userName);
			env.put(Context.SECURITY_CREDENTIALS, password);

			try {
				ctx = new InitialDirContext(env);
				//log.info("user " + userName + " LDAP login success.");
				return true;
			} catch (Exception e) {
			} finally {
				if (ctx != null) {
					try {
						ctx.close();
					} catch (NamingException e) {
						//log.error(e.getMessage(), e);
					}
				}
			}
		}
		return false;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public boolean exist(String userId) {
		AuthUser authUser = userDao.getById(userId);
		if(authUser == null)
			return false;
		else 
			return true;
	}

	public boolean add(AuthUser authUser) {
		return userDao.insert(authUser);
	}
	
	public void addLoginLog(String user_id) {
		AuthLoginLog log = new AuthLoginLog();
		log.setUser_id(user_id);
		log.setLogin_time(new Date());
		userDao.saveLoginLog(log);
	}
	
	public void addLogoutLog(String user_id) {
		AuthLoginLog log = new AuthLoginLog();
		log.setUser_id(user_id);
		log.setLogout_time(new Date());
		userDao.saveLogoutLog(log);
	}

	public List<AuthUser> findAll() {
		return userDao.findAll();
	}

	public boolean deleteById(String user_id) {
		return userDao.deleteById(user_id);
	}
	
	public AuthUser findById(String user_id) {
		return userDao.getById(user_id);
	}

	public List<AuthLoginLog> findAllLoginLog() {
		return userDao.findAllLoginLog();
	}
}

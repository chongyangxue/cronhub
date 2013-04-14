package org.cronhub.managesystem.commons.dao.bean;

import java.util.Date;

public class AuthLoginLog {
	private Long log_id;
	private String user_id;
	private Date login_time;
	private Date logout_time;

	public Long getLog_id() {
		return log_id;
	}

	public void setLog_id(Long log_id) {
		this.log_id = log_id;
	}

	public Date getLogin_time() {
		return login_time;
	}

	public void setLogin_time(Date login_time) {
		this.login_time = login_time;
	}

	public Date getLogout_time() {
		return logout_time;
	}

	public void setLogout_time(Date logout_time) {
		this.logout_time = logout_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}

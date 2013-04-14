package org.cronhub.managesystem.commons.dao.bean;

import java.util.Date;

public class Process {
	private Long id;
	private String user_id;
	private String process_name;
	private String cron_exp;
	private String description;
	private Date update_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcess_name() {
		return process_name;
	}

	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}

	public String getCron_exp() {
		return cron_exp;
	}

	public void setCron_exp(String cron_exp) {
		this.cron_exp = cron_exp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}

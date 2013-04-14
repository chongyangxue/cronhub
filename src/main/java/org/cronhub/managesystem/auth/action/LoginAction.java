package org.cronhub.managesystem.auth.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.cronhub.managesystem.auth.service.UserService;
import org.cronhub.managesystem.commons.dao.bean.AuthLoginLog;
import org.cronhub.managesystem.commons.dao.bean.AuthUser;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport{
	private static final long serialVersionUID = 6166185277488075690L;
	private UserService userService;
	private String user_id;
	private String password;
	private String flag;
	private List<AuthLoginLog> logList;

	public String loginCheck() {
		AuthUser user = userService.findById(user_id);
		if(user == null) {
			setFlag("not_exist");
			return ERROR;
		}
		if(userService.ldapCheck(user_id, password)) {
			ActionContext session = ActionContext.getContext();
			session.getSession().put("user_id", user_id);
			if(user.getUser_type().equals("1")) {
				session.getSession().put("userType", "admin");
			}else {
				session.getSession().put("userType", "user");
			}
			userService.addLoginLog(user_id);
			return SUCCESS;
		}
		else {
			setFlag("wrong_pwd");
			return ERROR;
		}
	}
	
	public String logout() {
		HttpSession session= ServletActionContext.getRequest().getSession();
		userService.addLogoutLog((String)session.getAttribute("user_id"));
		session.invalidate();
		return SUCCESS;
	}

	public String listLoginLog() {
		List<AuthLoginLog> logs = userService.findAllLoginLog();
		this.setLogList(logs);
		return SUCCESS;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<AuthLoginLog> getLogList() {
		return logList;
	}

	public void setLogList(List<AuthLoginLog> logList) {
		this.logList = logList;
	}
}

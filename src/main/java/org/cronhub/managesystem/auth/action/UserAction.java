package org.cronhub.managesystem.auth.action;

import java.util.List;

import org.cronhub.managesystem.auth.service.UserService;
import org.cronhub.managesystem.commons.dao.bean.AuthUser;
import org.cronhub.managesystem.commons.utils.PageIOUtils;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	private UserService userService;
	private AuthUser authUser;
	private List<AuthUser> userList;
	private String errCode;
	public String addUser() {
		if(userService.add(authUser))
			return SUCCESS;
		else {
			this.setErrCode("duplicate");
			return ERROR;
		}
	}

	public String listUser() {
		List<AuthUser> users = userService.findAll();
		this.setUserList(users);
		return SUCCESS;
	}

	public String deleteUser() {
		boolean result = userService.deleteById(authUser.getUser_id());
		if(result)
			PageIOUtils.printToPage("success");
		else
			PageIOUtils.printToPage("error");
		return NONE;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public List<AuthUser> getUserList() {
		return userList;
	}

	public void setUserList(List<AuthUser> userList) {
		this.userList = userList;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
}

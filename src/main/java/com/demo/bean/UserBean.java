package com.demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表
 * 
 * @author admin
 *
 */
public class UserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String userName;
	private String userPass;
	private String createTime;
	/**
	 * 用户的角色集合
	 */
	private List<RoleBean> roleList;

	public List<RoleBean> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleBean> roleList) {
		this.roleList = roleList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}

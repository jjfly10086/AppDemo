package com.demo.bean;

import java.io.Serializable;

public class RoleAuthBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String roleId;
	private String authId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

}

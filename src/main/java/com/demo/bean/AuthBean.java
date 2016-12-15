package com.demo.bean;

import java.io.Serializable;

/**
 * È¨ÏÞ±í
 * 
 * @author admin
 *
 */
public class AuthBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String authName;
	private String createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

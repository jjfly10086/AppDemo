package com.demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 * 
 * @author admin
 *
 */
public class RoleBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String roleName;
	private String createTime;
	/**
	 * 角色的权限集合
	 */
	private List<AuthBean> authList;

	
	public List<AuthBean> getAuthList() {
		return authList;
	}

	public void setAuthList(List<AuthBean> authList) {
		this.authList = authList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

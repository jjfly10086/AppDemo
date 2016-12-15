package com.demo.dao;

import java.util.List;

import com.demo.bean.AuthBean;
import com.demo.bean.RoleBean;
import com.demo.bean.UserBean;

public interface IUserDao {
	
	List<UserBean> queryList();
	
	UserBean findUserByUsername(String userName);
	
	/**
	 * 根据用户名查角色list
	 * @param userId
	 * @return
	 */
	List<RoleBean> queryRoleListByUserId(String userId);
	/**
	 * 根据角色名查权限list
	 * @param roleId
	 * @return
	 */
	List<AuthBean> queryAuthListByRoleId(String roleId);
}

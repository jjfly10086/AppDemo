package com.demo.service;

import java.util.List;

import com.demo.bean.AuthBean;
import com.demo.bean.RoleBean;
import com.demo.bean.UserBean;

public interface IUserService {
	List<UserBean> queryList();
	/**
	 * 根据用户名查找用户
	 * @param userName
	 * @return
	 */
	UserBean findUserByUsername(String userName);
	/**
	 * 根据用户id查角色list
	 * @param userId
	 * @return
	 */
	List<RoleBean> queryRoleListByUserId(String userId);
	/**
	 * 根据角色id查权限list
	 * @param roleId
	 * @return
	 */
	List<AuthBean> queryAuthListByRoleId(String roleId);
}

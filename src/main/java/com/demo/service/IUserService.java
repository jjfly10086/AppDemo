package com.demo.service;

import java.util.List;

import com.demo.bean.UserBean;

public interface IUserService {
	
	List<UserBean> queryList();
	/**
	 * 根据用户名查找用户
	 * @param userName
	 * @return
	 */
	UserBean findUserByTelephone(String telephone);
	/**
	 * 新建用户
	 * @param userBean
	 * @return
	 */
	int addUser(UserBean userBean);
}

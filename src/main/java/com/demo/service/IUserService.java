package com.demo.service;

import java.util.List;

import com.demo.bean.UserBean;

public interface IUserService {
	
	List<UserBean> queryList();
	/**
	 * �����û��������û�
	 * @param userName
	 * @return
	 */
	UserBean findUserByTelephone(String telephone);
	/**
	 * �½��û�
	 * @param userBean
	 * @return
	 */
	int addUser(UserBean userBean);
}

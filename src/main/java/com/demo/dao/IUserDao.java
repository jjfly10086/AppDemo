package com.demo.dao;

import java.util.List;

import com.demo.bean.UserBean;

public interface IUserDao {
	
	List<UserBean> queryList();
	
	UserBean findUserByTelephone(String telephone);
	
	int addUser(UserBean userBean);
}

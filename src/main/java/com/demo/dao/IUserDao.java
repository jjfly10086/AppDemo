package com.demo.dao;

import java.util.List;

import com.demo.bean.UserBean;

public interface IUserDao {
	
	List<UserBean> queryList();
	
	UserBean findUserByUsername(String userName);
}

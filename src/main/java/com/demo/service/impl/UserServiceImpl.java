package com.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.bean.UserBean;
import com.demo.dao.IUserDao;
import com.demo.service.IUserService;
@Service("userService")
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;
	@Override
	public List<UserBean> queryList() {
		return userDao.queryList();
	}
	/**
	 * 根据用户名查找用户
	 */
	@Override
	public UserBean findUserByTelephone(String telephone) {
		return userDao.findUserByTelephone(telephone);
	}
}	

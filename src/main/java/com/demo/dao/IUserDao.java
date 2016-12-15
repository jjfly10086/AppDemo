package com.demo.dao;

import java.util.List;

import com.demo.bean.AuthBean;
import com.demo.bean.RoleBean;
import com.demo.bean.UserBean;

public interface IUserDao {
	
	List<UserBean> queryList();
	
	UserBean findUserByUsername(String userName);
	
	/**
	 * �����û������ɫlist
	 * @param userId
	 * @return
	 */
	List<RoleBean> queryRoleListByUserId(String userId);
	/**
	 * ���ݽ�ɫ����Ȩ��list
	 * @param roleId
	 * @return
	 */
	List<AuthBean> queryAuthListByRoleId(String roleId);
}

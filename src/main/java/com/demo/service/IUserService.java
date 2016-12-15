package com.demo.service;

import java.util.List;

import com.demo.bean.AuthBean;
import com.demo.bean.RoleBean;
import com.demo.bean.UserBean;

public interface IUserService {
	List<UserBean> queryList();
	/**
	 * �����û��������û�
	 * @param userName
	 * @return
	 */
	UserBean findUserByUsername(String userName);
	/**
	 * �����û�id���ɫlist
	 * @param userId
	 * @return
	 */
	List<RoleBean> queryRoleListByUserId(String userId);
	/**
	 * ���ݽ�ɫid��Ȩ��list
	 * @param roleId
	 * @return
	 */
	List<AuthBean> queryAuthListByRoleId(String roleId);
}

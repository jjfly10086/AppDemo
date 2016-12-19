package com.demo.bean;

import java.io.Serializable;

public class UserBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String telephone;//�绰����
	private String userName;//�û���
	private String userPass;//����
	private String verifyCode;//��֤��
	private String verifyCodeSendtime;//����ʱ��
	private String createTime;//����ʱ��

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getVerifyCodeSendtime() {
		return verifyCodeSendtime;
	}

	public void setVerifyCodeSendtime(String verifyCodeSendtime) {
		this.verifyCodeSendtime = verifyCodeSendtime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

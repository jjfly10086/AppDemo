package com.demo.bean;

import java.io.Serializable;

/**
 * ͨ�÷��ؽ��Bean
 * 
 * @author admin
 *
 */
public class ResultBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;// ������
	private String msg;// ������Ϣ
	private Object data;// ��������

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

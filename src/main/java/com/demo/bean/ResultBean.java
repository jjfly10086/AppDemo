package com.demo.bean;

import java.io.Serializable;

/**
 * 通用返回结果Bean
 * 
 * @author admin
 *
 */
public class ResultBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;// 返回码
	private String msg;// 返回信息
	private Object data;// 返回数据

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

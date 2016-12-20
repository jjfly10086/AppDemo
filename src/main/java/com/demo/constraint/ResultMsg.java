package com.demo.constraint;
/**
 * 返回信息
 * @author admin
 *
 */
public interface ResultMsg {
	public static final String NULL_PARAMETER = "参数为空";
	public static final String OPERATING_SUCCESS = "操作成功";
	public static final String OPERATING_FAILURE = "操作失败";
	public static final String WRONG_MOBILE = "电话号码错误";
	public static final String USER_NOT_EXIST = "用户不存在";
	public static final String USER_ALREADY_EXIST = "用户已存在";
	public static final String SYSTEM_ERROR = "系统错误";
	public static final String WRONG_PASSWORD_ERROR = "密码错误";
	public static final String PASSWORD_PARSE_ERROR = "密码解析错误";
}

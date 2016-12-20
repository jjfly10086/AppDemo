package com.demo.utils;

import com.demo.bean.UserBean;

/**
 * String字符串工具类
 * @author admin
 *
 */
public class StringUtils {
	/**
	 * 判断对象是否为空，可判断空对象和空字符串
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object... obj){
		if(null == obj){
			return true;
		}
		for(Object o : obj){
			if(null == o){
				return true;
			}
			if(o instanceof String){
				if("".equals(o)){
					return true;
				}
			}
		}
		return false;
	}
	public static void main(String[] args) {
		String s = "1";
		String s1 = "";
		String s2 = "2";
		Float a = 22f;
		UserBean user = new UserBean();
		System.out.println(isEmpty(s,s1,s2,user,a));
	}
}

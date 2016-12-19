package com.demo.utils;
/**
 * String字符串工具类
 * @author admin
 *
 */
public class StringUtils {
	/**
	 * 判断字符串为空，对象为空和空字符串
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String... str){
		if(null == str){
			return true;
		}
		for(String s : str){
			if(null == s || "".equals(s)){
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) {
		String s = "1";
		String s1 = "1";
		String s2 = "";
		System.out.println(isEmpty(s,s1,s2));
	}
}

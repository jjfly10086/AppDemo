package com.demo.utils;

import com.demo.bean.UserBean;

/**
 * String�ַ���������
 * @author admin
 *
 */
public class StringUtils {
	/**
	 * �ж϶����Ƿ�Ϊ�գ����жϿն���Ϳ��ַ���
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

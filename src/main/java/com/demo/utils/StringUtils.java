package com.demo.utils;
/**
 * String�ַ���������
 * @author admin
 *
 */
public class StringUtils {
	/**
	 * �ж��ַ���Ϊ�գ�����Ϊ�պͿ��ַ���
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

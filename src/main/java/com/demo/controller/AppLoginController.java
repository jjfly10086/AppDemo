package com.demo.controller;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.bean.ResultBean;
import com.demo.bean.UserBean;
import com.demo.constraint.ResultCode;
import com.demo.constraint.ResultMsg;
import com.demo.redis.IRedisService;
import com.demo.service.IUserService;
import com.demo.utils.BeanUtils;
import com.demo.utils.HttpUtils;
import com.demo.utils.JacksonUtils;
import com.demo.utils.MD5Utils;
import com.demo.utils.RSAUtils;
import com.demo.utils.StringUtils;
import com.demo.utils.TokenUtils;

/**
 * app controller
 * 
 * @author admin
 *
 */
@Controller("appLoginController")
@RequestMapping("/app/login")
public class AppLoginController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserService userService;
	
	/**
	 * ��ȡ��Կ
	 * @return
	 */
	@RequestMapping("/getKey")
	@ResponseBody
	public ResultBean getPublicKey(){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.SUCCESS);
		result.setMsg(ResultMsg.OPERATING_SUCCESS);
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String publicKeyStr = (String)redisService.get("publicKey",String.class);
		result.setData(publicKeyStr);
		return result;
	}
	/**
	 * ��¼
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public ResultBean doLogin(String telephone,String userPass,HttpServletResponse res){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.FAILURE);
		result.setMsg(ResultMsg.OPERATING_FAILURE);
		//1.�жϷǿ�
		if(StringUtils.isEmpty(telephone,userPass)){
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.RSA��������
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String privateKeyStr = (String)redisService.get("privateKey",String.class);
		try {
			PrivateKey privateKey = RSAUtils.getPrivateKey(privateKeyStr);
			userPass = RSAUtils.decrypt(privateKey, userPass);
		} catch (Exception e) {
			logger.error("�����������",e);
			result.setMsg(ResultMsg.PASSWORD_PARSE_ERROR);
			return result;
		}
		//3.�����û�
		UserBean user =userService.findUserByTelephone(telephone);
		if(StringUtils.isEmpty(user)){
			result.setMsg(ResultMsg.USER_NOT_EXIST);
			return result;
		}else{
			//4.md5��֤
			if(!MD5Utils.verify(userPass, user.getUserPass())){//��֤����ʧ��
				result.setMsg(ResultMsg.WRONG_PASSWORD_ERROR);
				return result;
			}else{
				String tokenSecret = (String)redisService.get("tokenSecret",String.class);
				String token = TokenUtils.generateToken(user.getId(),tokenSecret);
				res.setHeader("Authorization", token);
				result.setCode(ResultCode.SUCCESS);
				result.setMsg(ResultMsg.OPERATING_SUCCESS);
			}
		}
		return result;
	}
	/**
	 * ��֤�û��Ƿ����
	 * @param telephone
	 * @return
	 */
	@RequestMapping("/checkUser")
	@ResponseBody
	public ResultBean checkUserExist(String telephone){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.FAILURE);
		result.setMsg(ResultMsg.OPERATING_FAILURE);
		//1.��֤�ǿ�
		if(StringUtils.isEmpty(telephone)){
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.�����û�
		UserBean user = userService.findUserByTelephone(telephone);
		if(StringUtils.isEmpty(user)){//�û�������
			result.setCode(ResultCode.SUCCESS);
			result.setMsg(ResultMsg.USER_NOT_EXIST);
			result.setData(false);
		}else{//�Ѵ���
			result.setCode(ResultCode.SUCCESS);
			result.setMsg(ResultMsg.USER_ALREADY_EXIST);
			result.setData(true);
		}
		return result;
	}
	/**
	 * ��֤��ע��
	 * @param telephone
	 * @param verifyCode 
	 * @param userPass
	 * @return
	 */
	@RequestMapping("/regist")
	@ResponseBody
	public ResultBean sendMsg(String telephone,String verifyCode,String userPass){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.FAILURE);
		result.setMsg(ResultMsg.OPERATING_FAILURE);
		//1.��֤�ǿ�
		if(StringUtils.isEmpty(telephone,verifyCode,userPass)){
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.����ת��
		IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
		PrivateKey privateKey;
		try {
			privateKey = RSAUtils.getPrivateKey((String)redisService.get("privateKey",String.class));
			userPass = RSAUtils.decrypt(privateKey, userPass);
		} catch (Exception e) {
			logger.error("�����������", e);
			result.setMsg(ResultMsg.PASSWORD_PARSE_ERROR);
			return result;
		}
		//3.�����û�
		UserBean userBean = userService.findUserByTelephone(telephone);
		if(!StringUtils.isEmpty(userBean)){
			result.setMsg(ResultMsg.USER_ALREADY_EXIST);
			return result;
		}
		//4.��֤��֤��
		String appKey = "1a0cc645325b2";
		String zone = "86";
		String params = "appKey="+appKey+"&phone="+telephone+"&zone="+zone+"&code="+verifyCode;
		String httpRes = HttpUtils.requestData("https://webapi.sms.mob.com/sms/verify", params);
		Map<String,Object> httpMap = null;
		try {
			httpMap = (Map<String,Object>)JacksonUtils.parseJson2Obj(httpRes, Map.class);
		} catch (IOException e) {
			logger.error("���������ؽ����������");
			result.setMsg(ResultMsg.SYSTEM_ERROR);
			return result;
		}
		if((Integer)httpMap.get("status")==200){
			//5.ע��
			userBean = new UserBean();
			userBean.setTelephone(telephone);
			userBean.setUserPass(MD5Utils.generate(userPass));
			userService.addUser(userBean);
			result.setCode(ResultCode.SUCCESS);
			result.setMsg(ResultMsg.OPERATING_SUCCESS);
		}else{//��֤ʧ��
			result.setMsg(httpMap.get("status")+"");
		}
		return result;
	}
}

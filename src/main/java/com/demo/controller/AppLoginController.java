package com.demo.controller;

import java.security.PrivateKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
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
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String publicKeyStr = (String)redisService.get("publicKey");
		result.setCode(ResultCode.SUCCESS);
		result.setMsg(ResultMsg.OPERATING_SUCCESS);
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
	public ResultBean doLogin(HttpServletRequest req,HttpServletResponse res){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.FAILURE);
		result.setMsg(ResultMsg.OPERATING_FAILURE);
		String telephone = req.getParameter("telephone");
		String userPass = req.getParameter("userPass");
		//1.�жϷǿ�
		if(StringUtils.isEmpty(telephone,userPass)){
			result.setCode(ResultCode.FAILURE);
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.RSA��������
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String privateKeyStr = (String)redisService.get("privateKey");
		try {
			PrivateKey privateKey = RSAUtils.getPrivateKey(privateKeyStr);
			userPass = RSAUtils.decrypt(privateKey, userPass);
		} catch (Exception e) {
			logger.error("�����������",e);
			return result;
		}
		//3.�����û�
		UserBean user =userService.findUserByTelephone(telephone);
		if(user != null){
			//4.md5��֤
			if(MD5Utils.verify(userPass, user.getUserPass())){
				String token = TokenUtils.generateToken(user.getId());
				res.setHeader("Authorization", token);
				result.setCode(ResultCode.SUCCESS);
				result.setMsg(ResultMsg.OPERATING_SUCCESS);
			}
		}
		return result;
	}
	@RequestMapping("/sendMsg")
	@ResponseBody
	public ResultBean sendMsg(String telephone){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.SUCCESS);
		result.setMsg(ResultMsg.OPERATING_SUCCESS);
		//1.��֤�ǿ�
		if(StringUtils.isEmpty(telephone)){
			result.setCode(ResultCode.FAILURE);
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.��֤�绰��ʽ
		Pattern pattern = Pattern.compile("^[1][0-9]{10}$");
		Matcher matcher = pattern.matcher(telephone);
		if(!matcher.matches()){
			result.setCode(ResultCode.FAILURE);
			result.setMsg(ResultMsg.WRONG_MOBILE);
			return result;
		}
		//3.�����û������������ڣ��½�
		UserBean userBean = userService.findUserByTelephone(telephone);
		if(null == userBean){
			
		}
		return result;
	}
}

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
	 * 获取密钥
	 * @return
	 */
	@RequestMapping("/getKey")
	@ResponseBody
	public ResultBean getPublicKey(){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.SUCCESS);
		result.setMsg(ResultMsg.OPERATING_SUCCESS);
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String publicKeyStr = (String)redisService.get("publicKey");
		result.setData(publicKeyStr);
		return result;
	}
	/**
	 * 登录
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
		//1.判断非空
		if(StringUtils.isEmpty(telephone,userPass)){
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.RSA解密密码
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String privateKeyStr = (String)redisService.get("privateKey");
		try {
			PrivateKey privateKey = RSAUtils.getPrivateKey(privateKeyStr);
			userPass = RSAUtils.decrypt(privateKey, userPass);
		} catch (Exception e) {
			logger.error("密码解析错误",e);
			result.setMsg(ResultMsg.PASSWORD_PARSE_ERROR);
			return result;
		}
		//3.查找用户
		UserBean user =userService.findUserByTelephone(telephone);
		if(StringUtils.isEmpty(user)){
			result.setMsg(ResultMsg.USER_NOT_EXIST);
			return result;
		}else{
			//4.md5验证
			if(!MD5Utils.verify(userPass, user.getUserPass())){//验证密码失败
				result.setMsg(ResultMsg.WRONG_PASSWORD_ERROR);
				return result;
			}else{
				String token = TokenUtils.generateToken(user.getId());
				res.setHeader("Authorization", token);
				result.setCode(ResultCode.SUCCESS);
				result.setMsg(ResultMsg.OPERATING_SUCCESS);
			}
		}
		return result;
	}
	/**
	 * 验证用户是否存在
	 * @param telephone
	 * @return
	 */
	@RequestMapping("/checkUser")
	@ResponseBody
	public ResultBean checkUserExist(String telephone){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.FAILURE);
		result.setMsg(ResultMsg.OPERATING_FAILURE);
		//1.验证非空
		if(StringUtils.isEmpty(telephone)){
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.查找用户
		UserBean user = userService.findUserByTelephone(telephone);
		if(StringUtils.isEmpty(user)){//用户不存在
			result.setCode(ResultCode.SUCCESS);
			result.setMsg(ResultMsg.USER_NOT_EXIST);
			result.setData(false);
		}else{//已存在
			result.setCode(ResultCode.SUCCESS);
			result.setMsg(ResultMsg.USER_ALREADY_EXIST);
			result.setData(true);
		}
		return result;
	}
	/**
	 * 
	 * @param telephone
	 * @return
	 */
	@RequestMapping("/sendMsg")
	@ResponseBody
	public ResultBean sendMsg(String telephone){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.FAILURE);
		result.setMsg(ResultMsg.OPERATING_FAILURE);
		//1.验证非空
		if(StringUtils.isEmpty(telephone)){
			result.setMsg(ResultMsg.NULL_PARAMETER);
			return result;
		}
		//2.验证电话格式
		Pattern pattern = Pattern.compile("^[1][0-9]{10}$");
		Matcher matcher = pattern.matcher(telephone);
		if(!matcher.matches()){
			result.setMsg(ResultMsg.WRONG_MOBILE);
			return result;
		}
		//3.查找用户，如若不存在，新建
		UserBean userBean = userService.findUserByTelephone(telephone);
		if(null == userBean){
			
		}
		return result;
	}
}

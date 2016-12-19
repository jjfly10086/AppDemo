package com.demo.controller;

import java.security.PrivateKey;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String publicKeyStr = (String)redisService.get("publicKey");
		result.setCode(ResultCode.SUCCESS);
		result.setMsg(ResultMsg.OPERATING_SUCCESS);
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
			result.setCode(ResultCode.FAILURE);
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
			return result;
		}
		//3.查找用户
		UserBean user =userService.findUserByUsername(telephone);
		if(user != null){
			//4.md5验证
			if(MD5Utils.verify(userPass, user.getUserPass())){
				String token = TokenUtils.generateToken(user.getId());
				res.setHeader("Authorization", token);
				result.setCode(ResultCode.SUCCESS);
				result.setMsg(ResultMsg.OPERATING_SUCCESS);
			}
		}
		return result;
	}
}

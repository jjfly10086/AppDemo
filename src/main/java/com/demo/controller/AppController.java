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
@RequestMapping("/app")
public class AppController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	@ResponseBody
	public ResultBean doLogin(HttpServletRequest req,HttpServletResponse res){
		ResultBean result = new ResultBean();
		String telephone = req.getParameter("telephone");
		String userPass = req.getParameter("userPass");
		//1.判断非空
		if(StringUtils.isEmpty(telephone,userPass)){
			result.setCode(ResultCode.FAILURE);
			result.setMsg(ResultMsg.NULL_PARAMETER);
		}
		//2.RSA解密密码
		IRedisService redisService = (IRedisService)BeanUtils.getBean("redisService");
		String privateKeyStr = (String)redisService.get("privateKey");
		try {
			PrivateKey privateKey = RSAUtils.getPrivateKey(privateKeyStr);
			userPass = RSAUtils.decrypt(privateKey, userPass);
		} catch (Exception e) {
			logger.error("密码解析错误",e);
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
	@ResponseBody
	@RequestMapping("/userList")
	public ModelMap getUser(){
		ModelMap model = new ModelMap();
		List<UserBean> userList = userService.queryList();
		model.addAttribute("userList", userList);
		return model;
	}
}

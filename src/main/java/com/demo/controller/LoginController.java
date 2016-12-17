package com.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.utils.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.bean.UserBean;
import com.demo.redis.IRedisService;
import com.demo.service.IUserService;
import com.demo.utils.BeanUtils;

import java.security.PublicKey;
import java.util.Map;


@Controller("loginController")
@RequestMapping("/login")
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private IUserService userService;
	/**
	 * 跳转登录界面
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String toLoginPage(ModelMap model) throws Exception{
        //2.从redis获取公钥返回页面
        IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
        String publicKeyStr = (String)redisService.get("publicKey");
        logger.info("返回公钥："+publicKeyStr);
        model.addAttribute("publicKey",publicKeyStr);
		return "login";
	}

	/**
	 * 登录
	 * @param req
	 * @param res
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest req, HttpServletResponse res,
			ModelMap modelMap) throws Exception{
		String userName = req.getParameter("userName");
		String userPass = req.getParameter("userPass");
		//1. 从redis获取私钥解密
        IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
        String privateKey = (String)redisService.get("privateKey");
		userName = RSAUtils.decrypt(RSAUtils.getPrivateKey(privateKey),userName);
        userPass = RSAUtils.decrypt(RSAUtils.getPrivateKey(privateKey),userPass);
        logger.info("解密后用户名："+userName+" ----------- 密码"+userPass);
        //2. 根据request获取用户名密码创建token
		UsernamePasswordToken token = new UsernamePasswordToken(userName,
				userPass);
		Subject subject = SecurityUtils.getSubject();
		String msg = "";
		try {
			subject.login(token);// 登录认证--->转到AuthenticationRealm的doGetAuthentication方法
			// 缓存redis
			//IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
			UserBean user = userService.findUserByUsername(userName);
			redisService.add(user.getUserName(),user);
			return "redirect:/index";
		} catch (AuthenticationException e) {// 认证失败抛出异常
			msg = "登录密码错误";
			logger.debug("登录密码错误");
		}
		//3.返回登录页
		modelMap.addAttribute("msg", msg);
		String publicKeyStr = (String)redisService.get("publicKey");
		logger.info("返回公钥："+publicKeyStr);
		modelMap.addAttribute("publicKey",publicKeyStr);
		return "login";
	}

	/**
	 * 登出
	 * @param req
	 * @param res
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/doLogout")
	public String doLogout(HttpServletRequest req, HttpServletResponse res,
			ModelMap modelMap) {
		Subject subject = SecurityUtils.getSubject();
		String userName = (String) subject.getPrincipal();
		IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
		redisService.del(userName);
		logger.debug(userName+"：清除缓存");
		logger.debug(userName+"：退出登录");
		subject.logout();
		return "login";
	}
}

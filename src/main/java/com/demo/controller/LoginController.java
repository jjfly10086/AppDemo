package com.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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

@Controller("loginController")
@RequestMapping("/login")
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private IUserService userService;
	/**
	 * ��ת��¼����
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String toLoginPage(ModelMap model) {
		return "login";
	}

	/**
	 * ��¼
	 * @param req
	 * @param res
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest req, HttpServletResponse res,
			ModelMap modelMap) {
		String userName = req.getParameter("userName");
		String userPass = req.getParameter("userPass");
		UsernamePasswordToken token = new UsernamePasswordToken(userName,
				userPass);// ����request��ȡ�û������봴��token
		Subject subject = SecurityUtils.getSubject();
		String msg = "";
		try {
			subject.login(token);// ��¼��֤--->ת��AuthenticationRealm��doGetAuthentication����
			// ����redis
			IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
			UserBean user = userService.findUserByUsername(userName);
			redisService.add(user.getUserName(),user);
			return "redirect:/index";
		} catch (AuthenticationException e) {// ��֤ʧ���׳��쳣
			msg = "��¼�������";
			logger.debug("��¼�������");
		}
		modelMap.addAttribute("msg", msg);
		return "login";
	}

	/**
	 * �ǳ�
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
		logger.debug(userName+"���������");
		logger.debug(userName+"���˳���¼");
		subject.logout();
		return "login";
	}
}

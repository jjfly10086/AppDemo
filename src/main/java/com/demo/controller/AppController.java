package com.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.bean.UserBean;
import com.demo.redis.IRedisService;
import com.demo.utils.BeanUtils;

/**
 * app controller
 * 
 * @author admin
 *
 */
@Controller("appLoginController")
@RequestMapping("/app")
public class AppController {

	/**
	 * 当前用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userList")
	public ModelMap getUser() {
		ModelMap model = new ModelMap();
		Subject subject = SecurityUtils.getSubject();
		String userName = (String) subject.getPrincipal();
		IRedisService redisService = (IRedisService) BeanUtils
				.getBean("redisService");
		UserBean user =(UserBean)redisService.get(userName);
		model.addAttribute("user", user);
		return model;
	}

}

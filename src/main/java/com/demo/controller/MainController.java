package com.demo.controller;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.bean.UserBean;
import com.demo.redis.IRedisService;
import com.demo.service.IUserService;
import com.demo.utils.BeanUtils;

@Controller("mainController")
@RequestMapping("/index")
public class MainController {
	
	@Resource
	private IUserService userService;
	
	@RequestMapping("")
	public String toIndex(ModelMap modelMap){
		return "index";
	}
	@RequestMapping("/list")
	@ResponseBody
	@RequiresPermissions("system:main")
	public ModelMap test(){
		ModelMap model = new ModelMap();
		model.addAttribute("userList", userService.queryList());
		return  model;
	}
	@ResponseBody
	@RequestMapping("/userList")
	public ModelMap getUser(){
		ModelMap model = new ModelMap();
		Subject subject = SecurityUtils.getSubject();
		String userName = (String)subject.getPrincipal();
		IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
		UserBean user = (UserBean)redisService.get(userName);
		model.addAttribute("user", user);
		return model;
	}
}

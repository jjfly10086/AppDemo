package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.bean.UserBean;
import com.demo.service.IUserService;

@Controller("testController")
@RequestMapping("/app/test")
public class TestController {
	
	@Autowired
	private IUserService userService;
	
	@ResponseBody
	@RequestMapping("/userList")
	public ModelMap getUser(){
		ModelMap model = new ModelMap();
		List<UserBean> userList = userService.queryList();
		model.addAttribute("userList", userList);
		return model;
	}
}

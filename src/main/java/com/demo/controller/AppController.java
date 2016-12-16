package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.bean.UserBean;
import com.demo.service.IUserService;
import com.demo.utils.MD5Utils;
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
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	@ResponseBody
	public Map<String,Object> doLogin(HttpServletRequest req,HttpServletResponse res){
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("code", "1");
		resultMap.put("msg", "用户名或密码错误");
		String userName = req.getParameter("userName");
		String userPass = req.getParameter("userPass");
		UserBean user =userService.findUserByUsername(userName);
		if(user != null){
			if(MD5Utils.verify(userPass, user.getUserPass())){
				String token = TokenUtils.generateToken(user.getId());
				res.setHeader("Authentication", token);
				resultMap.put("code", "0");
				resultMap.put("msg", "登录成功");
			}
		}
		return resultMap;
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

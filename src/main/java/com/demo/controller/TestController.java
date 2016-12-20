package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.bean.ResultBean;
import com.demo.bean.UserBean;
import com.demo.constraint.ResultCode;
import com.demo.constraint.ResultMsg;
import com.demo.service.IUserService;

@Controller("testController")
@RequestMapping("/app/test")
public class TestController {
	
	@Autowired
	private IUserService userService;
	
	@ResponseBody
	@RequestMapping("/userList")
	public ResultBean getUser(){
		ResultBean result = new ResultBean();
		result.setCode(ResultCode.SUCCESS);
		result.setMsg(ResultMsg.OPERATING_SUCCESS);
		List<UserBean> userList = userService.queryList();
		result.setData(userList);
		return result;
	}
}

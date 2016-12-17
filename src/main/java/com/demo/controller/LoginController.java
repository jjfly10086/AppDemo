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
	 * ��ת��¼����
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String toLoginPage(ModelMap model) throws Exception{
        //2.��redis��ȡ��Կ����ҳ��
        IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
        String publicKeyStr = (String)redisService.get("publicKey");
        logger.info("���ع�Կ��"+publicKeyStr);
        model.addAttribute("publicKey",publicKeyStr);
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
			ModelMap modelMap) throws Exception{
		String userName = req.getParameter("userName");
		String userPass = req.getParameter("userPass");
		//1. ��redis��ȡ˽Կ����
        IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
        String privateKey = (String)redisService.get("privateKey");
		userName = RSAUtils.decrypt(RSAUtils.getPrivateKey(privateKey),userName);
        userPass = RSAUtils.decrypt(RSAUtils.getPrivateKey(privateKey),userPass);
        logger.info("���ܺ��û�����"+userName+" ----------- ����"+userPass);
        //2. ����request��ȡ�û������봴��token
		UsernamePasswordToken token = new UsernamePasswordToken(userName,
				userPass);
		Subject subject = SecurityUtils.getSubject();
		String msg = "";
		try {
			subject.login(token);// ��¼��֤--->ת��AuthenticationRealm��doGetAuthentication����
			// ����redis
			//IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
			UserBean user = userService.findUserByUsername(userName);
			redisService.add(user.getUserName(),user);
			return "redirect:/index";
		} catch (AuthenticationException e) {// ��֤ʧ���׳��쳣
			msg = "��¼�������";
			logger.debug("��¼�������");
		}
		//3.���ص�¼ҳ
		modelMap.addAttribute("msg", msg);
		String publicKeyStr = (String)redisService.get("publicKey");
		logger.info("���ع�Կ��"+publicKeyStr);
		modelMap.addAttribute("publicKey",publicKeyStr);
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

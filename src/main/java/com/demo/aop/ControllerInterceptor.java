package com.demo.aop;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demo.utils.JacksonUtils;

@Aspect
public class ControllerInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Before("execution( * com.demo.controller.*.*(..))")
	public void doConBefore(JoinPoint point) throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		logger.info("����URL��"+request.getRequestURI());
		Map<String,String[]> paramMap = request.getParameterMap();
		logger.info("����Controller������" + point.getTarget().getClass() + "."
				+ point.getSignature().getName() + " ���������"
				+ JacksonUtils.parseObj2Json(paramMap));
	}
	@AfterReturning(value="execution( * com.demo.controller.*.*(..))",returning="returnObj")
	public void doConAfter(JoinPoint point,Object returnObj) throws IOException{
		if(returnObj instanceof String){
			logger.info("Controller���ؽ����" + point.getTarget().getClass() + "."
					+ point.getSignature().getName()+ " ������ͼ:" + "/WEB-INF/views/"+returnObj+".jsp");
		}else{
			logger.info("Controller���ؽ����" + point.getTarget().getClass() + "."
					+ point.getSignature().getName()+ " ���ؽ��:" + JacksonUtils.parseObj2Json(returnObj));
		}
	}
}

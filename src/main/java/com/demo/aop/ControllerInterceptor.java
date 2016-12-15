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
		logger.info("请求URL："+request.getRequestURI());
		Map<String,String[]> paramMap = request.getParameterMap();
		logger.info("请求Controller方法：" + point.getTarget().getClass() + "."
				+ point.getSignature().getName() + " 请求参数："
				+ JacksonUtils.parseObj2Json(paramMap));
	}
	@AfterReturning(value="execution( * com.demo.controller.*.*(..))",returning="returnObj")
	public void doConAfter(JoinPoint point,Object returnObj) throws IOException{
		if(returnObj instanceof String){
			logger.info("Controller返回结果：" + point.getTarget().getClass() + "."
					+ point.getSignature().getName()+ " 返回视图:" + "/WEB-INF/views/"+returnObj+".jsp");
		}else{
			logger.info("Controller返回结果：" + point.getTarget().getClass() + "."
					+ point.getSignature().getName()+ " 返回结果:" + JacksonUtils.parseObj2Json(returnObj));
		}
	}
}

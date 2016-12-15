package com.demo.aop;

import java.io.IOException;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.utils.JacksonUtils;

public class BizLogInterceptor {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static long excuteTime;

	public void doBizBefore(JoinPoint point) throws IOException {
		Object[] args = point.getArgs();
		// getTarget()获取切入点实体对象
		logger.info("请求Service方法：" + point.getTarget().getClass() + "."
				+ point.getSignature().getName() + " 请求参数："
				+ JacksonUtils.parseObj2Json(args));
		excuteTime = new Date().getTime();
	}

	public void doBizAfter(JoinPoint point, Object returnObj)
			throws IOException {
		excuteTime = new Date().getTime() - excuteTime;
		logger.info("Service执行结果：" + point.getTarget().getClass() + "."
				+ point.getSignature().getName() + " 执行时间：" + excuteTime + "ms"
				+ " 返回结果:" + JacksonUtils.parseObj2Json(returnObj));
	}
}

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
		// getTarget()��ȡ�����ʵ�����
		logger.info("����Service������" + point.getTarget().getClass() + "."
				+ point.getSignature().getName() + " ���������"
				+ JacksonUtils.parseObj2Json(args));
		excuteTime = new Date().getTime();
	}

	public void doBizAfter(JoinPoint point, Object returnObj)
			throws IOException {
		excuteTime = new Date().getTime() - excuteTime;
		logger.info("Serviceִ�н����" + point.getTarget().getClass() + "."
				+ point.getSignature().getName() + " ִ��ʱ�䣺" + excuteTime + "ms"
				+ " ���ؽ��:" + JacksonUtils.parseObj2Json(returnObj));
	}
}

package com.demo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePushJob {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void pushMsg(){
		logger.info("定时任务---------------------->");
	}
}

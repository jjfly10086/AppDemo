package com.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class MyRedisTemplate<K, V> {
	
	protected RedisTemplate<K, V> redisTemplate;

	/**
	 * ����redisTemplate
	 */
	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

//	/**
//	 * ��ȡ redisTemplate�Դ������л�����
//	 *
//	 */
//
//	protected RedisSerializer<String> getRedisSerializer() {
//		return redisTemplate.getStringSerializer();
//	}
}

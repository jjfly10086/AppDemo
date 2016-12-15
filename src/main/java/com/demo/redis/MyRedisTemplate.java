package com.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class MyRedisTemplate<K, V> {
	
	protected RedisTemplate<K, V> redisTemplate;

	/**
	 * 设置redisTemplate
	 */
	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

//	/**
//	 * 获取 redisTemplate自带的序列化对象
//	 *
//	 */
//
//	protected RedisSerializer<String> getRedisSerializer() {
//		return redisTemplate.getStringSerializer();
//	}
}

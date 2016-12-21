package com.demo.redis;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.demo.utils.JacksonUtils;

public class RedisServiceImpl extends MyRedisTemplate<String, Object> implements
		IRedisService {

	private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	/**
	 * 新增,对象转换为json字符串存取
	 */
	@Override
	public boolean add(final String key, final Object obj) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					byte[] keyByte = key.getBytes();
					byte[] value = null;
					if(obj instanceof String){
						String str = (String)obj;
						value = str.getBytes();
					}else{
						value = JacksonUtils.parseObj2Json(obj).getBytes();
					}
					connection.set(keyByte, value);
				} catch (Exception e) {
					logger.error("新增Redis key失败", e);
					return false;
				}
				return true;
			}
		});
		return result;
	}

	/**
	 * 删除
	 */
	@Override
	public boolean del(final String key) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					if (connection.del(key.getBytes()) > 0) {
						return true;
					} else {
						return false;
					}
				} catch (Exception e) {
					logger.error("删除Redis key失败", e);
				}
				return false;
			}
		});
		return result;
	}

	/**
	 * 获取对象字符串
	 */
	@Override
	public Object get(final String key) {
		Object obj = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				Object obj = null;
				try {
					obj = JacksonUtils.parseJson2Obj(new String(connection.get(key.getBytes())), Object.class);
				} catch (IOException e) {
					logger.error("JacksonUtils parse failed,Cause redis value is not a json string");
					obj = new String(connection.get(key.getBytes()));
				}
				return obj;
			}

		});
		return obj;
	}
}

package com.demo.redis;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.demo.utils.SerializeUtils;

public class RedisServiceImpl extends MyRedisTemplate<String, Object> implements
		IRedisService {

	private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	/**
	 * 新增
	 */
	@Override
	public boolean add(final String key, final Object obj) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					byte[] keyByte = SerializeUtils.serialize(key);
					byte[] value = SerializeUtils.serialize(obj);
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
					if (connection.del(SerializeUtils.serialize(key)) > 0) {
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
	 * 获取用户
	 */
	@Override
	public Object get(final String key) {
		Object obj = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				Object obj = null;
				try {
					obj = SerializeUtils.unserialize(connection
							.get(SerializeUtils.serialize(key)));
				} catch (ClassNotFoundException | IOException e) {
					logger.error("获取Redis key失败", e);
				}
				return obj;
			}

		});
		return obj;
	}
}

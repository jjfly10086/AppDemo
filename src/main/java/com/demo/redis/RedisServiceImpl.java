package com.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.demo.bean.UserBean;
import com.demo.utils.SerializeUtils;

public class RedisServiceImpl extends MyRedisTemplate<String, Object>
		implements IRedisService {

	private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	/**
	 * 新增
	 */
	@Override
	public boolean add(String key,Object obj) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {

				byte[] keyByte = SerializeUtils.serialize(key);
				byte[] value = SerializeUtils.serialize(obj);
				try {
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
	public boolean del(String key) {
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
	public Object get(String key) {
		Object obj = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				Object obj = SerializeUtils.unserialize(connection.get(SerializeUtils.serialize(key)));
				return obj;
			}
			
		});
		return obj;
	}
}

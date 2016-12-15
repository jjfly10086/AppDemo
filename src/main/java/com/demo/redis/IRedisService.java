package com.demo.redis;

public interface IRedisService {
	Object get(String key);
	boolean add(String key,Object obj);
	boolean del(String key);
}

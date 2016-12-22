package com.demo.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {
	public static void main(String[] args) throws InterruptedException {
		
		Jedis jedis = new Jedis("103.237.0.165", 6379);
		jedis.auth("xxxxxx");
		jedis.set("name","xinxin");//
		System.out.println(jedis.get("name"));//
	}
}


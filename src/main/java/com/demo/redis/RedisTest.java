package com.demo.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisTest {
	public static void main(String[] args) throws InterruptedException {
		
//		Jedis jedis = new Jedis("103.237.0.165", 6379);
//		jedis.auth("xxxxxx");
//		jedis.set("name","xinxin");//向key-->name中放入了value-->xinxin  
//		System.out.println(jedis.get("name"));//执行结果：xinxin  
		PubClient pubClient = new PubClient("103.237.0.165",6379);
		final String channel = "pubsub-channel";
		pubClient.pub(channel, "message1");
		pubClient.pub(channel, "message2");
		Thread.sleep(2000);
		Thread subThread = new Thread(new Runnable(){
			@Override
			public void run() {
				SubClient subClient = new SubClient("103.237.0.165", 6379);
				System.out.println("消息处理开始");
				JedisPubSub listener = new PrintListener();
				subClient.sub(listener, channel);
				System.out.println("消息处理结束");
			}
		});
		subThread.start();
		int i=0;
		while(i<10){
			String message = "message pub"+i;
			pubClient.pub(channel, message);
			i++;
			Thread.sleep(1000);
		}
		pubClient.close(channel);
	}
}
//消息处理器
class PrintListener extends JedisPubSub{
	@Override
	public void onMessage(String channel, String message) {
		// TODO Auto-generated method stub
		System.out.println("message receive："+message+",channel："+channel+"..."+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if(message.equalsIgnoreCase("quit")){
			this.unsubscribe(channel);
		}
	}
}
//消息发布端
class PubClient {
	private Jedis jedis;
	public PubClient(String host,int port){
		jedis = new Jedis(host,port);
		jedis.auth("jjfly10086");
	}
	public void pub(String channel,String message){
		jedis.publish(channel, message);
	}
	public void close(String channel){
		jedis.publish(channel, "quit");
		jedis.del(channel);
	}
}
//消息订阅端
class SubClient{
	private Jedis jedis;
	public SubClient(String host,int port){
		jedis = new Jedis(host,port);
		jedis.auth("jjfly10086");
	}
	public void sub(JedisPubSub listener,String channel){
		jedis.subscribe(listener, channel);
	}
}

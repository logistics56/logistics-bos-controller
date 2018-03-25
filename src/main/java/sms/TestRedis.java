package sms;

import redis.clients.jedis.Jedis;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月25日 下午11:29:38
* 
*/
public class TestRedis {
	public static void main(String[] args) {
		
		Jedis jedis = new Jedis("localhost");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
	}
}

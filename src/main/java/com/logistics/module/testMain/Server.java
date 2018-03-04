/**
 * Dianping.com Inc.
 * Copyright (c) 2003-2013 All Rights Reserved.
 */
package com.logistics.module.testMain;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Server {


	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring/spring-pigeonServer.xml"});
    	context.start();
    	System.in.read(); // 按任意键退出
	}

}

package com.logistics.module.response;

import java.util.Date;

import com.logistics.module.response.base.BaseResponse;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月15日 下午3:40:24
* 
*/
public class OrderProgressResponse{
	
	private String recName;
	
	private String transitInfo;
	
	private Date time;

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public String getTransitInfo() {
		return transitInfo;
	}

	public void setTransitInfo(String transitInfo) {
		this.transitInfo = transitInfo;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "OrderProgressResponse [recName=" + recName + ", transitInfo=" + transitInfo + "]";
	}
	
}

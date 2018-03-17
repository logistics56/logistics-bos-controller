package com.logistics.module.request;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月17日 上午9:06:53
* 
*/
public class CourierRequest {
	String ids;
	
	String cCourierNum;

	public String getcCourierNum() {
		return cCourierNum;
	}

	public void setcCourierNum(String cCourierNum) {
		this.cCourierNum = cCourierNum;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
}

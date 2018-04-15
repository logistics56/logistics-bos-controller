package com.logistics.module.request;

import java.util.Date;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月15日 上午9:42:53
* 
*/
public class TransitInfoRequest {
	
	private int transitInfoId;
	
	private String operation;
	
	private String address;
	
	private String description;
	
	private int courierNum;
	
	private String courierName;
	
	private String signName;
	
	private Date signTime;
	
	private String signType;
	
	private String errorRemark;

	public int getTransitInfoId() {
		return transitInfoId;
	}

	public void setTransitInfoId(int transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCourierNum() {
		return courierNum;
	}

	public void setCourierNum(int courierNum) {
		this.courierNum = courierNum;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getErrorRemark() {
		return errorRemark;
	}

	public void setErrorRemark(String errorRemark) {
		this.errorRemark = errorRemark;
	}

	@Override
	public String toString() {
		return "TransitInfoRequest [transitInfoId=" + transitInfoId + ", operation=" + operation + ", address="
				+ address + ", description=" + description + ", courierNum=" + courierNum + ", courierName="
				+ courierName + ", signName=" + signName + ", signTime=" + signTime + ", signType=" + signType
				+ ", errorRemark=" + errorRemark + "]";
	}

}

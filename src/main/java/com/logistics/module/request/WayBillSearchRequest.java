package com.logistics.module.request;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月14日 上午10:07:59
* 
*/
public class WayBillSearchRequest extends PageRequest{
	  String orderNum;
	  String sendAddress;
	  String recAddress;
	  String sendProNum;
	  String signStatus;
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getRecAddress() {
		return recAddress;
	}
	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
	}
	public String getSendProNum() {
		return sendProNum;
	}
	public void setSendProNum(String sendProNum) {
		this.sendProNum = sendProNum;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	@Override
	public String toString() {
		return "WayBillSearchRequest [orderNum=" + orderNum + ", sendAddress=" + sendAddress + ", recAddress="
				+ recAddress + ", sendProNum=" + sendProNum + ", signStatus=" + signStatus + "]";
	}
	
}

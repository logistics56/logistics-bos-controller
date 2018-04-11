package com.logistics.module.request;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月11日 下午10:37:44
* 
*/
public class WayBillRequest {
	private int orderId;
	private String cOrderNum;
	private String cSendProNum;
	private String cSendName;
	private String cSendAddress;
	private String cSendCompany;
	private String cSendMobile;
	private String cRecName;
	private String cRecAddress;
	private String cRecCompany;
	private String cRecMobile;
	private String num;
	private String realNum;
	private String cWeight;
	private String actlweit;
	private String cGoodsType;
	private String vol;
	private String cPayTypeNum;
	private String price;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getcOrderNum() {
		return cOrderNum;
	}
	public void setcOrderNum(String cOrderNum) {
		this.cOrderNum = cOrderNum;
	}
	public String getcSendProNum() {
		return cSendProNum;
	}
	public void setcSendProNum(String cSendProNum) {
		this.cSendProNum = cSendProNum;
	}
	public String getcSendName() {
		return cSendName;
	}
	public void setcSendName(String cSendName) {
		this.cSendName = cSendName;
	}
	public String getcSendAddress() {
		return cSendAddress;
	}
	public void setcSendAddress(String cSendAddress) {
		this.cSendAddress = cSendAddress;
	}
	public String getcSendCompany() {
		return cSendCompany;
	}
	public void setcSendCompany(String cSendCompany) {
		this.cSendCompany = cSendCompany;
	}
	public String getcSendMobile() {
		return cSendMobile;
	}
	public void setcSendMobile(String cSendMobile) {
		this.cSendMobile = cSendMobile;
	}
	public String getcRecName() {
		return cRecName;
	}
	public void setcRecName(String cRecName) {
		this.cRecName = cRecName;
	}
	public String getcRecAddress() {
		return cRecAddress;
	}
	public void setcRecAddress(String cRecAddress) {
		this.cRecAddress = cRecAddress;
	}
	public String getcRecCompany() {
		return cRecCompany;
	}
	public void setcRecCompany(String cRecCompany) {
		this.cRecCompany = cRecCompany;
	}
	public String getcRecMobile() {
		return cRecMobile;
	}
	public void setcRecMobile(String cRecMobile) {
		this.cRecMobile = cRecMobile;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getRealNum() {
		return realNum;
	}
	public void setRealNum(String realNum) {
		this.realNum = realNum;
	}
	public String getcWeight() {
		return cWeight;
	}
	public void setcWeight(String cWeight) {
		this.cWeight = cWeight;
	}
	public String getActlweit() {
		return actlweit;
	}
	public void setActlweit(String actlweit) {
		this.actlweit = actlweit;
	}
	public String getcGoodsType() {
		return cGoodsType;
	}
	public void setcGoodsType(String cGoodsType) {
		this.cGoodsType = cGoodsType;
	}
	public String getVol() {
		return vol;
	}
	public void setVol(String vol) {
		this.vol = vol;
	}
	public String getcPayTypeNum() {
		return cPayTypeNum;
	}
	public void setcPayTypeNum(String cPayTypeNum) {
		this.cPayTypeNum = cPayTypeNum;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "WayBillRequest [orderId=" + orderId + ", cOrderNum=" + cOrderNum + ", cSendProNum=" + cSendProNum
				+ ", cSendName=" + cSendName + ", cSendAddress=" + cSendAddress + ", cSendCompany=" + cSendCompany
				+ ", cSendMobile=" + cSendMobile + ", cRecName=" + cRecName + ", cRecAddress=" + cRecAddress
				+ ", cRecCompany=" + cRecCompany + ", cRecMobile=" + cRecMobile + ", num=" + num + ", realNum="
				+ realNum + ", cWeight=" + cWeight + ", actlweit=" + actlweit + ", cGoodsType=" + cGoodsType + ", vol="
				+ vol + ", cPayTypeNum=" + cPayTypeNum + ", price=" + price + "]";
	}
	
}

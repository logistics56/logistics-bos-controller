package com.logistics.module.request;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月4日 上午12:38:13
* 
*/
public class OrderRequest {
	private String telephone;
	//寄件人信息
	private String sendName;
	private String sendMobile;
	private String sendCompany;
	private String sendAddress;
	private String sendAreaInfo;
	//收件人信息
	private String recName;
	private String recMobile;
	private String recCompany;
	private String recAddress;
	private String recAreaInfo;
	//快件信息
	private String goodsType;
	private String weight;
	private String remark;
	private String sendProNum;
	private String payTypeNum;
	private String sendMobileMsg;
	private String price;
	private String vol;
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getSendMobile() {
		return sendMobile;
	}
	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}
	public String getSendCompany() {
		return sendCompany;
	}
	public void setSendCompany(String sendCompany) {
		this.sendCompany = sendCompany;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getSendAreaInfo() {
		return sendAreaInfo;
	}
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	public String getRecName() {
		return recName;
	}
	public void setRecName(String recName) {
		this.recName = recName;
	}
	public String getRecMobile() {
		return recMobile;
	}
	public void setRecMobile(String recMobile) {
		this.recMobile = recMobile;
	}
	public String getRecCompany() {
		return recCompany;
	}
	public void setRecCompany(String recCompany) {
		this.recCompany = recCompany;
	}
	public String getRecAddress() {
		return recAddress;
	}
	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
	}
	public String getRecAreaInfo() {
		return recAreaInfo;
	}
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSendProNum() {
		return sendProNum;
	}
	public void setSendProNum(String sendProNum) {
		this.sendProNum = sendProNum;
	}
	public String getPayTypeNum() {
		return payTypeNum;
	}
	public void setPayTypeNum(String payTypeNum) {
		this.payTypeNum = payTypeNum;
	}
	public String getSendMobileMsg() {
		return sendMobileMsg;
	}
	public void setSendMobileMsg(String sendMobileMsg) {
		this.sendMobileMsg = sendMobileMsg;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVol() {
		return vol;
	}
	public void setVol(String vol) {
		this.vol = vol;
	}
	@Override
	public String toString() {
		return "OrderRequest [telephone=" + telephone + ", sendName=" + sendName + ", sendMobile=" + sendMobile
				+ ", sendCompany=" + sendCompany + ", sendAddress=" + sendAddress + ", sendAreaInfo=" + sendAreaInfo
				+ ", recName=" + recName + ", recMobile=" + recMobile + ", recCompany=" + recCompany + ", recAddress="
				+ recAddress + ", recAreaInfo=" + recAreaInfo + ", goodsType=" + goodsType + ", weight=" + weight
				+ ", remark=" + remark + ", sendProNum=" + sendProNum + ", payTypeNum=" + payTypeNum
				+ ", sendMobileMsg=" + sendMobileMsg + ", price=" + price + ", vol=" + vol + "]";
	}
	
}

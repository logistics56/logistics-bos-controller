package com.logistics.module.request;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月2日 下午12:57:53
* 
*/
public class CustomerUpdateRequest {
	
	private String username;
	
	private int sex;
	
	private Date birth;
	
	private String address;
	
	private String mobile;
	
	private String company;
	
	private String department;
	
	private String position;
	
	private String telephone;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "CustomerUpdateRequest [username=" + username + ", sex=" + sex + ", birth=" + birth + ", address="
				+ address + ", mobile=" + mobile + ", company=" + company + ", department=" + department + ", position="
				+ position + ", telephone=" + telephone + "]";
	}

}

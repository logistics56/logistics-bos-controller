package com.logistics.module.request;

import java.util.Arrays;
import java.util.Date;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月6日 下午3:15:27
* 
*/
public class UserRequest extends PageRequest{
	
	private Integer usernum;
	private String username;
	private String password;
	private String nickname;
	private String telephone;
	private Date birthday;
	private String gender;
	private String remark;
	private String[] roleIds;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	public Integer getUsernum() {
		return usernum;
	}
	public void setUsernum(Integer usernum) {
		this.usernum = usernum;
	}
	@Override
	public String toString() {
		return "UserRequest [usenum=" + usernum + ", username=" + username + ", password=" + password + ", nickname="
				+ nickname + ", telephone=" + telephone + ", birthday=" + birthday + ", gender=" + gender + ", remark="
				+ remark + ", roleIds=" + Arrays.toString(roleIds) + "]";
	}
	
}

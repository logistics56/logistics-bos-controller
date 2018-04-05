package com.logistics.module.request;

import java.util.Arrays;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月5日 下午8:26:19
* 
*/
public class RoleRequest {
	
	 private String cDescription;
	 
	 private String cKeyword;
	 
	 private String cName;
	  
	 private String[] permissionIds;
	 
	 private String menuIds;//以逗号分割

	public String getcDescription() {
		return cDescription;
	}

	public void setcDescription(String cDescription) {
		this.cDescription = cDescription;
	}

	public String getcKeyword() {
		return cKeyword;
	}

	public void setcKeyword(String cKeyword) {
		this.cKeyword = cKeyword;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String[] getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Override
	public String toString() {
		return "RoleRequest [cDescription=" + cDescription + ", cKeyword=" + cKeyword + ", cName=" + cName
				+ ", permissionIds=" + Arrays.toString(permissionIds) + ", menuIds=" + menuIds + "]";
	}


}

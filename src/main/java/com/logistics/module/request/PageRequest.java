package com.logistics.module.request;
/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月10日 下午12:33:45
* 
*/
public class PageRequest {
	
	private int page;
	
	private int rows;
	
	private String cCourierNum;
	
	private String searchStr;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getcCourierNum() {
		return cCourierNum;
	}

	public void setcCourierNum(String cCourierNum) {
		this.cCourierNum = cCourierNum;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	
	

}

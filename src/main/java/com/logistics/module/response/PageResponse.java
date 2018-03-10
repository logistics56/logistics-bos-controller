package com.logistics.module.response;

import java.util.List;

import com.logistics.module.response.base.BaseResponse;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月10日 下午12:26:03
* 
*/
public class PageResponse{
	
	private int total;
	
    private List rows;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
    
    

}

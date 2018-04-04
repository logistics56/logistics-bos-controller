package com.logistics.module.response;

import java.util.List;

import com.logistics.module.dto.OrderDTO;
import com.logistics.module.response.base.BaseResponse;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月4日 下午5:09:15
* 
*/
public class OrderResponse extends BaseResponse{
	List<OrderDTO> orders;

	public List<OrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderResponse [orders=" + orders + "]";
	}
	
}

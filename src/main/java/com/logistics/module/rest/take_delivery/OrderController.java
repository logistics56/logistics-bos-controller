package com.logistics.module.rest.take_delivery;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.request.OrderRequest;
import com.logistics.module.response.base.BaseResponse;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月4日 上午12:36:21
* 
*/
@RestController
@RequestMapping("/order")
public class OrderController {

	/**
	 * 下单
	 * @param ref
	 * @return
	 */
	@RequestMapping(value = "/placeAnOrder", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody OrderRequest ref) {
		BaseResponse response = new BaseResponse();
		System.out.println(ref.toString());
		return response;
	}
}

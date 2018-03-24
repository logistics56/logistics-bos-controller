package com.logistics.module.crm.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.crm.dto.CustomerDTO;
import com.logistics.module.crm.service.CustomerService;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月24日 下午10:22:08
* 
*/

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@RequestMapping(value = "/queryByFixedAreaId", method = { RequestMethod.POST })
	public PageResponse queryByFixedAreaId(PageRequest ref) {
		PageResponse response = new PageResponse();
		int total = customerService.queryTotalByFixedAreaId(ref.getNodeID());
		List<CustomerDTO> rows = customerService.queryByFixedAreaId(ref.getNodeID());
		
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	

}

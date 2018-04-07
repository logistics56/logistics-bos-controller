package com.logistics.module.rest.take_delivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.CourierDTO;
import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.WorkBillResponse;
import com.logistics.module.service.CourierService;
import com.logistics.module.service.OrderService;
import com.logistics.module.service.WorkBillService;
import com.logistics.module.util.TimeUtils;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月7日 下午1:40:21
* 
*/
@RestController
@RequestMapping("/workBill")
public class WorkBillController {
	
	@Autowired
	WorkBillService workBillService;
	
	@Autowired
	CourierService courierService;
	
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = workBillService.queryNoReceiveTotal(null);
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<WorkBillDTO> rows1 = workBillService.queryNoReceiveByPage( pageNum, ref.getRows(), null);
		List<WorkBillResponse> rows = new ArrayList<WorkBillResponse>();
		if(!CollectionUtils.isEmpty(rows1)){
			for (WorkBillDTO workBillDTO : rows1) {
				WorkBillResponse row = new WorkBillResponse();
				CourierDTO courier = courierService.selectByPrimaryKey(workBillDTO.getcCourier());
				if(courier != null){
					row.setUserName(courier.getcName());
				}
				OrderDTO order = orderService.selectByPrimaryKey(workBillDTO.getcOrderId());
				if(order != null){
					row.setOrderNum(order.getcOrderNum());
					row.setAddress(order.getcSendAddress());
				}
				row.setTime(workBillDTO.getcBuildtime());
				rows.add(row);
			}
		}
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/queryPageAlertData", method = { RequestMethod.POST })
	public PageResponse queryPageAlertData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = workBillService.queryNoReceiveTotal(new Date(TimeUtils.addDate(System.currentTimeMillis(),-1)));
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<WorkBillDTO> rows1 = workBillService.queryNoReceiveByPage( pageNum, ref.getRows(), new Date(TimeUtils.addDate(System.currentTimeMillis(),-1)));
		List<WorkBillResponse> rows = new ArrayList<WorkBillResponse>();
		if(!CollectionUtils.isEmpty(rows1)){
			for (WorkBillDTO workBillDTO : rows1) {
				WorkBillResponse row = new WorkBillResponse();
				CourierDTO courier = courierService.selectByPrimaryKey(workBillDTO.getcCourier());
				if(courier != null){
					row.setUserName(courier.getcName());
				}
				OrderDTO order = orderService.selectByPrimaryKey(workBillDTO.getcOrderId());
				if(order != null){
					row.setOrderNum(order.getcOrderNum());
					row.setAddress(order.getcSendAddress());
				}
				row.setTime(workBillDTO.getcBuildtime());
				rows.add(row);
			}
		}
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/querySelfPageData", method = { RequestMethod.POST })
	public PageResponse querySelfPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		CourierDTO user = courierService.queryByNum(ref.getcCourierNum());
		if(user != null){
			int total = workBillService.queryNoReceiveTotalByCourierId(user.getcId(), null);
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<WorkBillDTO> rows1 = workBillService.queryNoReceiveByPageByCourierId(user.getcId(), pageNum, ref.getRows(),null);
			List<WorkBillResponse> rows = new ArrayList<WorkBillResponse>();
			if(!CollectionUtils.isEmpty(rows1)){
				for (WorkBillDTO workBillDTO : rows1) {
					WorkBillResponse row = new WorkBillResponse();
					CourierDTO courier = courierService.selectByPrimaryKey(workBillDTO.getcCourier());
					if(courier != null){
						row.setUserName(courier.getcName());
					}
					OrderDTO order = orderService.selectByPrimaryKey(workBillDTO.getcOrderId());
					if(order != null){
						row.setOrderNum(order.getcOrderNum());
						row.setAddress(order.getcSendAddress());
					}
					row.setTime(workBillDTO.getcBuildtime());
					rows.add(row);
				}
			}
			response.setTotal(total);
			response.setRows(rows);
		}
		return response;
	}
	
	@RequestMapping(value = "/querySelfPageAlertData", method = { RequestMethod.POST })
	public PageResponse querySelfPageAlertData(PageRequest ref){
		PageResponse response = new PageResponse();
		CourierDTO user = courierService.queryByNum(ref.getcCourierNum());
		if(user != null){
			int total = workBillService.queryNoReceiveTotalByCourierId(user.getcId(), new Date(TimeUtils.addDate(System.currentTimeMillis(),-1)));
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<WorkBillDTO> rows1 = workBillService.queryNoReceiveByPageByCourierId(user.getcId(), pageNum, ref.getRows(), new Date(TimeUtils.addDate(System.currentTimeMillis(),-1)));
			List<WorkBillResponse> rows = new ArrayList<WorkBillResponse>();
			if(!CollectionUtils.isEmpty(rows1)){
				for (WorkBillDTO workBillDTO : rows1) {
					WorkBillResponse row = new WorkBillResponse();
					CourierDTO courier = courierService.selectByPrimaryKey(workBillDTO.getcCourier());
					if(courier != null){
						row.setUserName(courier.getcName());
					}
					OrderDTO order = orderService.selectByPrimaryKey(workBillDTO.getcOrderId());
					if(order != null){
						row.setOrderNum(order.getcOrderNum());
						row.setAddress(order.getcSendAddress());
					}
					row.setTime(workBillDTO.getcBuildtime());
					rows.add(row);
				}
			}
			response.setTotal(total);
			response.setRows(rows);
		}
		return response;
	}

}

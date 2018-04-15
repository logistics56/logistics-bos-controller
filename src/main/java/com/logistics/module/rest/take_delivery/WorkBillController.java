package com.logistics.module.rest.take_delivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.CourierDTO;
import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.DeleteIds;
import com.logistics.module.request.OrderRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.WorkBillResponse;
import com.logistics.module.response.base.BaseResponse;
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
					row.setcId(workBillDTO.getcId());
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
	
	@RequestMapping(value = "/updateDataStatus", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		System.out.println(ids);
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			workBillService.updateState(Integer.valueOf(str));
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}
	
	
	@RequestMapping(value = "/queryAllData", method = { RequestMethod.POST })
	public PageResponse queryAllData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int id =0;
		if(StringUtils.isEmpty(ref.getSearchStr())){
			id = 0;
		}else{
			id = Integer.valueOf(ref.getSearchStr());
		}
		
		int total = workBillService.queryTotal(id);
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<WorkBillDTO> rows = workBillService.queryByPage(id, pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		return response;
	}
	
	@RequestMapping(value = "/changeWorkBill", method = { RequestMethod.POST })
	public BaseResponse changeWorkBill(@RequestBody OrderRequest ref){
		BaseResponse response = new BaseResponse();
		
		WorkBillDTO workBill = workBillService.selectByPrimaryKey(ref.getId());
		if(workBill != null){
			orderService.updateOrderType("2", ref.getCourierId(), workBill.getcOrderId());
			workBillService.updateCourierId(ref.getCourierId(), ref.getId());
		}
		
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}

}

package com.logistics.module.rest.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.AreaDTO;
import com.logistics.module.dto.CourierDTO;
import com.logistics.module.dto.StandardDTO;
import com.logistics.module.dto.SubAreaDTO;
import com.logistics.module.dto.TakeTimeDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.CourierRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.CourierResponse;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.SubAreaResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.CourierService;
import com.logistics.module.service.StandardService;
import com.logistics.module.service.TakeTimeService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月17日 上午8:39:15
* 
*/
@RestController
@RequestMapping("/courier")
public class CourierController {
	
	@Autowired
	StandardService standardService;
	
	@Autowired
	CourierService courierService;
	
	@Autowired
	TakeTimeService takeTimeService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		if(StringUtils.isEmpty(ref.getcCourierNum())){
			int total = courierService.queryTotal();
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<CourierDTO> resultRows = courierService.queryByPage( pageNum, ref.getRows());
			List<CourierResponse> rows =new ArrayList<CourierResponse>();
			if(org.springframework.util.CollectionUtils.isEmpty(resultRows)){
				return response;
			}
			for (CourierDTO resultRow : resultRows) {
				StandardDTO standardDTO = standardService.selectByPrimaryKey(resultRow.getcStandardId());
				if(standardDTO == null){
					return response;
				}
				CourierResponse courierResponse = new CourierResponse();
				BeanUtils.copyProperties(resultRow, courierResponse);
				courierResponse.setcStandardName(standardDTO.getcName());
				rows.add(courierResponse);
			}
			response.setTotal(total);
			response.setRows(rows);
		}else{
			CourierDTO courierDTO = courierService.queryByNum(ref.getcCourierNum());
			if(courierDTO == null ){
				return null;
			}
			StandardDTO standardDTO = standardService.selectByPrimaryKey(courierDTO.getcStandardId());
			if(standardDTO == null){
				return null;
			}
			CourierResponse courierResponse = new CourierResponse();
			BeanUtils.copyProperties(courierDTO, courierResponse);
			courierResponse.setcStandardName(standardDTO.getcName());
			List<CourierResponse> rows =new ArrayList<CourierResponse>();
			rows.add(courierResponse);
			response.setTotal(1);
			response.setRows(rows);
		}
		
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody CourierDTO ref){
		BaseResponse response = new BaseResponse();
		int num= 0;
		if(ref.getcId() == null){
			int id = courierService.queryMaxId() + 1;
			ref.setcId(id);
			ref.setcCourierNum("1000"+id);
			num = courierService.insertSelective(ref);
		}else{
			num = courierService.updateByPrimaryKey(ref);
		}
		if(num == 1){
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
			response.setResult(ResponseCode.SUCCESS.getCode());
		}else{
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
			response.setResult(ResponseCode.FAILED.getCode());
		}
		return response;
	}
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody CourierRequest ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			int id = Integer.valueOf(str);
			courierService.deleteData(id);
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}
	
	
	@RequestMapping(value = "/queryByFixedAreaId", method = { RequestMethod.POST })
	public PageResponse queryByFixedAreaId(PageRequest ref) {
		PageResponse response = new PageResponse();
		int total = courierService.queryTotalByFixedAreaId(ref.getNodeID());
		List<CourierDTO> rows1 = courierService.queryByFixedAreaId(ref.getNodeID());
		List<CourierResponse> rows = new ArrayList<CourierResponse>();
		if(org.springframework.util.CollectionUtils.isEmpty(rows1)){
			return response;
		}
		for (CourierDTO r : rows1) {
			StandardDTO standardDTO = standardService.selectByPrimaryKey(r.getcStandardId());
			if(standardDTO == null){
				return response;
			}
			TakeTimeDTO takeTimeDTO = takeTimeService.selectByPrimaryKey(r.getcTaketimeId());
			if(takeTimeDTO == null){
				return response;
			}
			CourierResponse courierResponse = new CourierResponse();
			BeanUtils.copyProperties(r, courierResponse);
			courierResponse.setcStandardName(standardDTO.getcName());
			courierResponse.setTakeTimeName(takeTimeDTO.getcName());
			rows.add(courierResponse);
		}
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
}

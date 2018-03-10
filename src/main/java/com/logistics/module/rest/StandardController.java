package com.logistics.module.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.StandardDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.StandardRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.StandardService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月10日 下午12:29:52
* 
*/
@RestController
@RequestMapping("/standard")
public class StandardController {
	
	@Autowired
	StandardService standardService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(StandardRequest ref){
		PageResponse response = new PageResponse();
		
		int total = standardService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<StandardDTO> rows = standardService.queryByPage( pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody StandardDTO ref){
		BaseResponse response = new BaseResponse();
		int num= 0;
		if(ref.getcId() == null){
			int id = standardService.queryMaxId() + 1;
			ref.setcId(id);
			ref.setcOperatingTime(new Date());
			num = standardService.insertSelective(ref);
		}else{
			ref.setcOperatingTime(new Date());
			num = standardService.updateByPrimaryKey(ref);
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

}

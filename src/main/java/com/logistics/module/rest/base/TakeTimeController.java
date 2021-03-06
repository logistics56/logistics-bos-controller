package com.logistics.module.rest.base;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.TakeTimeDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.TakeTimeService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月24日 上午9:47:02
* 
*/
@RestController
@RequestMapping("/takeTime")
public class TakeTimeController {
	
	@Autowired
	TakeTimeService takeTimeService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = takeTimeService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<TakeTimeDTO> rows = takeTimeService.queryByPage( pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody TakeTimeDTO ref){
		BaseResponse response = new BaseResponse();
		int num= 0;
		if(ref.getcId() == null){
			int id = takeTimeService.queryMaxId() + 1;
			ref.setcId(id);
			ref.setcOperatingTime(new Date());
			num = takeTimeService.insertSelective(ref);
		}else{
			ref.setcOperatingTime(new Date());
			num = takeTimeService.updateByPrimaryKey(ref);
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
	
	@RequestMapping(value = "/queryAll", method = { RequestMethod.POST })
	public List<TakeTimeDTO> queryAll(){
		PageResponse response = new PageResponse();
		
		List<TakeTimeDTO> rows = takeTimeService.queryAll();
		
		return rows;
	}

}

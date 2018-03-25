package com.logistics.module.crm.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.crm.dto.CustomerDTO;
import com.logistics.module.crm.dto.SmsSignupDTO;
import com.logistics.module.crm.service.CustomerService;
import com.logistics.module.crm.service.SmsSignupService;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.util.TimeUtils;

import sms.SmsRequest;

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
	
	@Autowired
	SmsSignupService smsSignupService;
	
	@RequestMapping(value = "/queryByFixedAreaId", method = { RequestMethod.POST })
	public PageResponse queryByFixedAreaId(PageRequest ref) {
		PageResponse response = new PageResponse();
		int total = customerService.queryTotalByFixedAreaId(ref.getNodeID());
		List<CustomerDTO> rows = customerService.queryByFixedAreaId(ref.getNodeID());
		
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/queryByTelephone", method = { RequestMethod.POST })
	public BaseResponse queryByFixedAreaId(@RequestBody CustomerDTO ref) {
		BaseResponse response = new BaseResponse();
		List<CustomerDTO> list = customerService.queryByTelephone(ref.getcTelephone());
		if(CollectionUtils.isEmpty(list)){
			response.setResult(ResponseCode.SUCCESS.getCode());
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		}else{
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
		}
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody SmsRequest ref) {
		BaseResponse response = new BaseResponse();
		//查询验证码
		List<SmsSignupDTO> list = smsSignupService.queryByTime(new Date(TimeUtils.addMinute(System.currentTimeMillis(),-5)), new Date(), ref.getTelephone(), ref.getSource());
		if(CollectionUtils.isEmpty(list)){
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("验证码已失效，请重新获取");
		}else{
			SmsSignupDTO dto = list.get(0);
			String sqlCheckcode = dto.getcCheckcode().toLowerCase();
			String htmlCheckcode = ref.getCheckcode().toLowerCase();
			if(!htmlCheckcode.equals(sqlCheckcode)){
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("输入的验证码有错，请重新输入");
			}else{
				CustomerDTO cus = new CustomerDTO();
				cus.setcTelephone(ref.getTelephone());
				cus.setcUsername(ref.getUsername());
				cus.setcPassword(ref.getPassword());
				cus.setcEmail(ref.getEmail());
				
				int num = customerService.insertSelective(cus);
				if(num == 1){
					response.setResult(ResponseCode.SUCCESS.getCode());
					response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
				}else{
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg("当前注册人数过多，注册失败，请重新注册");
				}
				
			}
		}
		return response;
	}
	

}

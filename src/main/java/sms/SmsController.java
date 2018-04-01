package sms;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.logistics.module.crm.dto.SmsSignupDTO;
import com.logistics.module.crm.service.SmsSignupService;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;

import sms.util.AliSmsUtils;


/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月25日 下午4:35:18
* 
*/
@RestController
@RequestMapping("/sms")
public class SmsController {
	
	@Autowired
	SmsSignupService smsSignupService;
	
	@RequestMapping(value = "/sendSms", method = { RequestMethod.POST })
	public BaseResponse queryByFixedAreaId(@RequestBody SmsRequest ref) {
		BaseResponse response = new BaseResponse();
		String randomCode = RandomStringUtils.randomAlphanumeric(4);
		
		System.out.println(randomCode);
		
		SmsSignupDTO record = new SmsSignupDTO();
		record.setcTelephone(ref.getTelephone());
		record.setcCheckcode(randomCode);
		record.setcSource(ref.getSource());
		record.setcUsername(ref.getUsername());
		record.setcCreatetime(new Date());
		record.setcUpdatetime(new Date());
		int num = smsSignupService.insertSelective(record);
		if(num == 1){
//         发送短信------------
			if(AliSmsUtils.status == 1){
				try {
					SendSmsResponse smsResponse = AliSmsUtils.sendSms(ref.getTelephone(), ref.getUsername(), randomCode);
					 if(smsResponse.getCode() != null && smsResponse.getCode().equals("OK")) { 
						 System.out.println("发送短信成功!");
					 }
				} catch (ClientException e) {
					e.printStackTrace();
				}
			}
			
			response.setResult(ResponseCode.SUCCESS.getCode());
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		}else{
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
		}
		return response;
	}

}

package com.logistics.module.crm.rest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.logistics.module.crm.dto.CustomerDTO;
import com.logistics.module.crm.dto.SmsSignupDTO;
import com.logistics.module.crm.service.CustomerService;
import com.logistics.module.crm.service.SmsSignupService;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.CustomerUpdateRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.util.TimeUtils;

import sms.SmsRequest;
import sms.util.MailUtils;

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
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
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
				response.setErrorMsg("短信验证码错误...");
			}else{
				CustomerDTO cus = new CustomerDTO();
				cus.setcTelephone(ref.getTelephone());
				cus.setcUsername(ref.getUsername());
				cus.setcPassword(ref.getPassword());
				cus.setcEmail(ref.getEmail());
				
				int num = customerService.insertSelective(cus);
				if(num == 1){//注册成功
					
					// 发送一封激活邮件
					// 生成激活码
					String activecode = RandomStringUtils.randomNumeric(32);

					// 将激活码保存到redis，设置24小时失效
					redisTemplate.opsForValue().set(ref.getTelephone(), activecode, 24,
							TimeUnit.HOURS);
					
					// 调用MailUtils发送激活邮件
					String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
							+ MailUtils.activeUrl + "?telephone=" + ref.getTelephone()
							+ "&activecode=" + activecode + "'>駃达快递邮箱绑定地址</a>";
					MailUtils.sendMail("駃达快递激活邮件", content, ref.getEmail());
					
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
	
	@RequestMapping(value = "/activeMail", method = { RequestMethod.GET })
	public BaseResponse activeMail(SmsRequest ref) throws IOException{
		BaseResponse response = new BaseResponse();
		// 判断激活码是否有效
		String activecodeRedis = redisTemplate.opsForValue().get(
				ref.getTelephone());
		if (activecodeRedis == null || !activecodeRedis.equals(ref.getActivecode())) {
			// 激活码无效
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("激活码无效，请登录系统，重新绑定邮箱！");
		}else {
			// 激活码有效
			// 防止重复绑定
			// 调用CRM webService 查询客户信息，判断是否已经绑定
						
			//根据手机号查询客户
			List<CustomerDTO> customers = customerService.queryByTelephone(ref.getTelephone());
			
			if(!CollectionUtils.isEmpty(customers) ) {
				
				if (customers.get(0).getcType() == 1) {
					// 已经绑定过
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg("邮箱已经绑定过，无需重复绑定！");
				} else {
					// 没有绑定,进行绑定
					//更新type-----------------------------------------
					
					int num = customerService.updateType(ref.getTelephone());
					//更新type-----------------------------------------
					if(num == 1){
						response.setResult(ResponseCode.SUCCESS.getCode());
						response.setErrorMsg("邮箱绑定成功！");
						// 删除redis的激活码
						redisTemplate.delete(ref.getTelephone());
					}else{
						response.setResult(ResponseCode.FAILED.getCode());
						response.setErrorMsg("邮箱绑定失败！");
					}
				}
			}

			
		}
		return response;
	}
	
	@RequestMapping(value = "/signin", method = { RequestMethod.POST })
	public BaseResponse signin(@RequestBody SmsRequest ref) {
		BaseResponse response = new BaseResponse();
		if(ref.getSelect() == 1){
			//查询验证码
			List<SmsSignupDTO> list = smsSignupService.queryByTime(new Date(TimeUtils.addMinute(System.currentTimeMillis(),-5)), new Date(), ref.getTelephone(), ref.getSource());
			if(CollectionUtils.isEmpty(list)){
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("验证码错误或不存在");
			}else{
				SmsSignupDTO dto = list.get(0);
				String sqlCheckcode = dto.getcCheckcode().toLowerCase();
				String htmlCheckcode = ref.getCheckcode().toLowerCase();
				if(!htmlCheckcode.equals(sqlCheckcode)){
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg("短信验证码错误...");
				}else{
						List<CustomerDTO> customers = customerService.queryByTelephone(ref.getTelephone());
						customers.get(0).setcPassword("");
						response.setUser(customers.get(0));
						response.setResult(ResponseCode.SUCCESS.getCode());
						response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
				}
			}
		}else if(ref.getSelect() == 0){
			//根据手机号查询客户
			List<CustomerDTO> customers = customerService.queryByTelephone(ref.getTelephone2());
			
			if(!CollectionUtils.isEmpty(customers) ) {
				if(customers.get(0).getcPassword().equals(ref.getPassword())){
					response.setUser(customers.get(0));
					response.setResult(ResponseCode.SUCCESS.getCode());
					response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
				}else{
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg("密码输入错误");
				}
			}else{
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("账号不存在");
			}
		}else{
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("请选择登录方式");
		}
		return response;
	}
	
	@RequestMapping(value = "/forgotPassword", method = { RequestMethod.POST })
	public BaseResponse forgotPassword(@RequestBody SmsRequest ref) {
		BaseResponse response = new BaseResponse();
		//根据手机号查询客户
		List<CustomerDTO> customers = customerService.queryByTelephone(ref.getTelephone());
		
		if(CollectionUtils.isEmpty(customers) ) {
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("账号不存在");
		}else{
			if(customers.get(0).getcType() == 0){
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("该账号还未绑定邮箱，暂不支持此操作，建议你使用手机号登录");
			}else{
				
				// 发送一封密码找回邮件
				// 调用MailUtils发送激活邮件
				String content = "尊敬的："+customers.get(0).getcUsername()+"，您好，<br/>感谢您使用駃达快递服务，您正在进行账号密码取回，<br/>你的密码为 :"+customers.get(0).getcPassword()+"，<br/>如非本人操作，请忽略此邮件，由此给您带来的不便请谅解！ <br/><br/>本邮件由[<a href='http://localhost:8083/logistics-customer-web/login.html'>駃达快递</a>]系统发出，请勿直接回复";
				MailUtils.sendMail("駃达快递密码找回邮件", content, customers.get(0).getcEmail());
				
				response.setResult(ResponseCode.SUCCESS.getCode());
				response.setErrorMsg("找回密码成功，密码已发送至你的邮箱，请及时查看");
			}
		}
				
		return response;
	}
	
	@RequestMapping(value = "/updateFile", method = { RequestMethod.POST })
	public BaseResponse updateFile(MultipartFile file,String telephone) {
		BaseResponse response = new BaseResponse();
		System.out.println(file);
		System.out.println(telephone);
		String path = "customerPhotos/";
		String fileName = ""+telephone+TimeUtils.getFormatedDateTime(new Date()) +".jpg";
		File dir = new File(path, fileName);
		if(!dir.exists()){  
            dir.mkdirs();  
        }  
		//      MultipartFile自带的解析方法  
        try {
			file.transferTo(dir);
		} catch (IllegalStateException e) {
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
			e.printStackTrace();
		} catch (IOException e) {
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
			e.printStackTrace();
		}  
        List<CustomerDTO> list = customerService.queryByTelephone(telephone);
        if(CollectionUtils.isEmpty(list)){
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
		}else{
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setcId(list.get(0).getcId());
			customerDTO.setcImg(fileName);
			int num = customerService.updateByPrimaryKeySelective(customerDTO);
			if(num == 1){
				response.setResult(ResponseCode.SUCCESS.getCode());
				response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
			}else{
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg(ResponseCode.FAILED.getMsg());
			}
		}
		return response;
	}
	@RequestMapping(value = "/updateInfo", method = { RequestMethod.POST })
	public BaseResponse updateInfo(@RequestBody CustomerUpdateRequest ref) {
		BaseResponse response = new BaseResponse();
		 List<CustomerDTO> list = customerService.queryByTelephone(ref.getTelephone());
	        if(CollectionUtils.isEmpty(list)){
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg(ResponseCode.FAILED.getMsg());
			}else{
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setcId(list.get(0).getcId());
				customerDTO.setcUsername(ref.getUsername());
				customerDTO.setcSex(ref.getSex());
				customerDTO.setcBrithday(ref.getBirth());
				customerDTO.setcAddress(ref.getAddress());
				customerDTO.setcMobilephone(ref.getMobile());
				customerDTO.setcCompany(ref.getCompany());
				customerDTO.setcDepartment(ref.getDepartment());
				customerDTO.setcPosition(ref.getPosition());
				int num = customerService.updateByPrimaryKeySelective(customerDTO);
				if(num == 1){
					List<CustomerDTO> list2 = customerService.queryByTelephone(ref.getTelephone());
					list2.get(0).setcPassword("");
					response.setUser(list2.get(0));
					response.setResult(ResponseCode.SUCCESS.getCode());
					response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
				}else{
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg(ResponseCode.FAILED.getMsg());
				}
				
			}
		return response;
	}
	
	@RequestMapping(value = "/binding", method = { RequestMethod.POST })
	public BaseResponse binding(@RequestBody CustomerUpdateRequest ref) {
		BaseResponse response = new BaseResponse();
		List<CustomerDTO> list = customerService.queryByTelephone(ref.getTelephone());
        if(CollectionUtils.isEmpty(list)){
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("账号错误");
		}else{
			if(list.get(0).getcType() == 1){
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("该账号已绑定邮箱");
			}else{
				// 发送一封激活邮件
				// 生成激活码
				String activecode = RandomStringUtils.randomNumeric(32);
				
				// 将激活码保存到redis，设置24小时失效
				redisTemplate.opsForValue().set(ref.getTelephone(), activecode, 24,
						TimeUnit.HOURS);
				
				// 调用MailUtils发送激活邮件
				String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
						+ MailUtils.activeUrl + "?telephone=" + ref.getTelephone()
						+ "&activecode=" + activecode + "'>駃达快递邮箱绑定地址</a>";
				MailUtils.sendMail("駃达快递激活邮件", content, ref.getEmail());
				
				response.setResult(ResponseCode.SUCCESS.getCode());
				response.setErrorMsg("邮件已发送至你的邮箱，请于24小时内，进行邮箱账户的绑定");
			}
		}			
		return response;
	}
	
	@RequestMapping(value = "/changepwd", method = { RequestMethod.POST })
	public BaseResponse changepwd(@RequestBody CustomerUpdateRequest ref) {
		BaseResponse response = new BaseResponse();
		if(StringUtils.isEmpty(ref.getOldpwd()) || StringUtils.isEmpty(ref.getNewpwd()) || StringUtils.isEmpty(ref.getSecpwd())){
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("请填写全部信息");
		}else{
			if(!ref.getNewpwd().equals(ref.getSecpwd())){
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("两次输入的密码不一致，请确认后提交");
			}else{
				List<CustomerDTO> list = customerService.queryByTelephone(ref.getTelephone());
		        if(CollectionUtils.isEmpty(list)){
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg("账号错误");
				}else{
					if(!list.get(0).getcPassword().equals(ref.getOldpwd())){
						response.setResult(ResponseCode.FAILED.getCode());
						response.setErrorMsg("原始密码输入错误，请填写正确信息");
					}else{
						CustomerDTO customerDTO = new CustomerDTO();
						customerDTO.setcPassword(ref.getNewpwd());
						customerDTO.setcId(list.get(0).getcId());
						int num = customerService.updateByPrimaryKeySelective(customerDTO);
						if(num == 1){
							response.setResult(ResponseCode.SUCCESS.getCode());
							response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
						}else{
							response.setResult(ResponseCode.FAILED.getCode());
							response.setErrorMsg("修改失败");
						}
					}
				}
			}
		}
		return response;
	}

}

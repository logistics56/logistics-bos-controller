package com.logistics.module.rest.take_delivery;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.logistics.module.crm.dto.CustomerDTO;
import com.logistics.module.crm.service.CustomerService;
import com.logistics.module.dto.AreaDTO;
import com.logistics.module.dto.CourierDTO;
import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.SubAreaDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.CourierRequest;
import com.logistics.module.request.OrderRequest;
import com.logistics.module.response.OrderResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.AreaService;
import com.logistics.module.service.CourierService;
import com.logistics.module.service.OrderService;
import com.logistics.module.service.SubAreaService;
import com.logistics.module.service.WorkBillService;

import sms.enums.SMSModel;
import sms.util.AliSmsUtils;
import sms.util.MailUtils;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月4日 上午12:36:21
* 
*/
@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CourierService courierService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	WorkBillService workBillService;
	
	@Autowired
	SubAreaService subAreaService;

	/**
	 * 下单
	 * @param ref
	 * @return
	 */
	@RequestMapping(value = "/placeAnOrder", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody OrderRequest ref) {
		BaseResponse response = new BaseResponse();
		//生成工单号
		String orderNum = UUID.randomUUID().toString();
		
		OrderDTO order = coverOrder(ref);
		order.setcOrderNum(orderNum);
		
		List<CustomerDTO> list = customerService.queryByTelephone(ref.getTelephone());
		if(CollectionUtils.isEmpty(list)){
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
		}else{
			CustomerDTO customer = list.get(0);
			
			order.setcCustomerId(customer.getcId());
			
			if(customer.getcAddress().equals(ref.getSendAddress())){
				//地址匹配成功
				if(!StringUtils.isEmpty(customer.getcFixedAreaId())){
					//已关联定区
					String fixedAreaId = customer.getcFixedAreaId();
					
					isOrder(order, orderNum, fixedAreaId, response, ref);
					
				}else{
					//未关联定区
					int label = 0;
					String fixedAreaId = "";
					List<SubAreaDTO> subAreaList = subAreaService.queryAll();
					if(CollectionUtils.isEmpty(subAreaList)){
						response.setResult(ResponseCode.FAILED.getCode());
						response.setErrorMsg(ResponseCode.FAILED.getMsg());
					}else{
						for (SubAreaDTO subAreaDTO : subAreaList) {
							if(label == 0){
								if(ref.getSendAddress().contains(subAreaDTO.getcKeyWords())){
									label = 1;
									fixedAreaId = subAreaDTO.getcFixedareaId();
								}
							}
						}
						if(label == 0){
							for (SubAreaDTO subAreaDTO : subAreaList) {
								if(label == 0){
									if(ref.getSendAddress().contains(subAreaDTO.getcAssistKeyWords())){
										label = 1;
										fixedAreaId = subAreaDTO.getcFixedareaId();
									}
								}
							}
						}
						if(label == 0){
							if(!StringUtils.isEmpty(order.getcSendAreaId())){
								for (SubAreaDTO subAreaDTO : subAreaList) {
									if(label == 0){
										if(order.getcSendAreaId().equals(subAreaDTO.getcAreaId())){
											label = 1;
											fixedAreaId = subAreaDTO.getcFixedareaId();
										}
									}
								}
							}
						}
						if(label ==1){
							customerService.updateFixedAreaId(fixedAreaId, customer.getcId());
							isOrder(order, orderNum, fixedAreaId, response, ref);
						}else{
							order.setcOrderType("0");
							int num = orderService.insertSelective(order);
							if(num == 1){
								// 调用MailUtils发送激活邮件
								String content = "订单："+orderNum+"下单失败，请尽快进行人工分单";
								MailUtils.sendMail("駃达快递提醒邮件", content, "1594064654@qq.com");
								response.setResult(ResponseCode.SUCCESS.getCode());
								response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
							}else{
								response.setResult(ResponseCode.FAILED.getCode());
								response.setErrorMsg(ResponseCode.FAILED.getMsg());
							}
						}
					}
					
				}
			}else{
				//地址匹配失败
				int label = 0;
				String fixedAreaId = "";
				List<SubAreaDTO> subAreaList = subAreaService.queryAll();
				if(CollectionUtils.isEmpty(subAreaList)){
					response.setResult(ResponseCode.FAILED.getCode());
					response.setErrorMsg(ResponseCode.FAILED.getMsg());
				}else{
					for (SubAreaDTO subAreaDTO : subAreaList) {
						if(label == 0){
							if(ref.getSendAddress().contains(subAreaDTO.getcKeyWords())){
								label = 1;
								fixedAreaId = subAreaDTO.getcFixedareaId();
							}
						}
					}
					if(label == 0){
						for (SubAreaDTO subAreaDTO : subAreaList) {
							if(label == 0){
								if(ref.getSendAddress().contains(subAreaDTO.getcAssistKeyWords())){
									label = 1;
									fixedAreaId = subAreaDTO.getcFixedareaId();
								}
							}
						}
					}
					if(label == 0){
						if(!StringUtils.isEmpty(order.getcSendAreaId())){
							for (SubAreaDTO subAreaDTO : subAreaList) {
								if(label == 0){
									if(order.getcSendAreaId().equals(subAreaDTO.getcAreaId())){
										label = 1;
										fixedAreaId = subAreaDTO.getcFixedareaId();
									}
								}
							}
						}
					}
					if(label ==1){
						isOrder(order, orderNum, fixedAreaId, response, ref);
					}else{
						order.setcOrderType("0");
						int num = orderService.insertSelective(order);
						if(num == 1){
							// 调用MailUtils发送激活邮件
							String content = "订单："+orderNum+"下单失败，请尽快进行人工分单";
							MailUtils.sendMail("駃达快递提醒邮件", content, "1594064654@qq.com");
							response.setResult(ResponseCode.SUCCESS.getCode());
							response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
						}else{
							response.setResult(ResponseCode.FAILED.getCode());
							response.setErrorMsg(ResponseCode.FAILED.getMsg());
						}
					}
				}
			}
		}
		
		
		return response;
	}
	
	private OrderDTO coverOrder(OrderRequest ref){
		OrderDTO order = new OrderDTO();
		order.setcSendName(ref.getSendName());
		order.setcSendMobile(ref.getSendMobile());
		order.setcSendCompany(ref.getSendCompany());
		order.setcSendAddress(ref.getSendAddress());
		order.setcRecName(ref.getRecName());
		order.setcRecMobile(ref.getRecMobile());
		order.setcRecCompany(ref.getRecCompany());
		order.setcRecAddress(ref.getRecAddress());
		order.setcGoodsType(ref.getGoodsType());
		order.setcWeight(Double.valueOf(ref.getWeight()));
		order.setcRemark(ref.getRemark());
		order.setcSendProNum(ref.getSendProNum());
		order.setcPayTypeNum(ref.getPayTypeNum());
		order.setcSendMobileMsg(ref.getSendMobileMsg());
		order.setcOrderTime(new Date());
		order.setcStatus("1");
		
		String sendAreaId = queryAreaID(ref.getSendAreaInfo());
		if(!StringUtils.isEmpty(sendAreaId)){
			order.setcSendAreaId(sendAreaId);
		}
		String recAreaId = queryAreaID(ref.getRecAreaInfo());
		if(!StringUtils.isEmpty(recAreaId)){
			order.setcSendAreaId(recAreaId);
		}
		
		return order;
		
	}
	
	private String queryAreaID(String msg){
		String[] area = msg.split("/");//山东省/临沂市/兰陵县
		if(area.length<2){
			return null;
		}
		if(area.length == 2){
			List<AreaDTO> dtos = areaService.queryByPCD(area[0], area[1], null);
			if(CollectionUtils.isEmpty(dtos)){
				return null;
			}else{
				return dtos.get(0).getcId();
			}
		}else{
			List<AreaDTO> dtos = areaService.queryByPCD(area[0], area[1], area[2]);
			if(CollectionUtils.isEmpty(dtos)){
				return null;
			}else{
				return dtos.get(0).getcId();
			}
		}
	}
	
	private void isOrder(OrderDTO order, String orderNum, String fixedAreaId, BaseResponse response, OrderRequest ref){
		List<CourierDTO> list2 = courierService.queryByFixedAreaId(fixedAreaId);
		if(CollectionUtils.isEmpty(list2)){
			//匹配快递员失败，发送邮箱通知客服
			order.setcOrderType("0");
			int num = orderService.insertSelective(order);
			if(num == 1){
				// 调用MailUtils发送激活邮件
				String content = "定区："+fixedAreaId+"未绑定快递员，请尽完成快绑定，并为订单："+orderNum+"人工分单";
				MailUtils.sendMail("駃达快递提醒邮件", content, "1594064654@qq.com");
				response.setResult(ResponseCode.SUCCESS.getCode());
				response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
			}else{
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg(ResponseCode.FAILED.getMsg());
			}
		}else{
			//匹配快递员成功，发送短信生成工单
			order.setcOrderType("1");
			//随机分配一个快递员
			Random rand = new Random();
			int randNum = rand.nextInt(list2.size());
			CourierDTO courier = list2.get(randNum);
			order.setcCourierId(courier.getcId());
			
			int num = orderService.insertSelective(order);
			if(num == 1){
				OrderDTO orderdto = orderService.queryByOrderNum(orderNum);
				if(orderdto != null){
					//发送短信------------
					if(AliSmsUtils.status == 1){
						try {
							SendSmsResponse smsResponse = AliSmsUtils.sendSms(courier.getcTelephone(), null, orderdto.getcId()+"", SMSModel.SMS_129764430.getValve());
							 if(smsResponse.getCode() != null && smsResponse.getCode().equals("OK")) { 
								 System.out.println("发送短信成功!");
							 }else{
								 AliSmsUtils.sendSms(courier.getcTelephone(), null, orderNum, SMSModel.SMS_129764430.getValve());
							 }
						} catch (ClientException e) {
							e.printStackTrace();
						}
					}
				}
				//生成工单
				WorkBillDTO workBill = new WorkBillDTO();
				workBill.setcAttachbilltimes(0);
				workBill.setcBuildtime(new Date());
				workBill.setcPickstate("0");
				workBill.setcRemark(ref.getRemark());
				workBill.setcSmsnumber(SMSModel.SMS_129764430.getValve());
				workBill.setcType("已通知");
				workBill.setcCourier(courier.getcId());
				
				if(orderdto != null){
					workBill.setcOrderId(orderdto.getcId());
				}
				workBillService.insertSelective(workBill);
				response.setResult(ResponseCode.SUCCESS.getCode());
				response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
				
				
			}else{
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg(ResponseCode.FAILED.getMsg());
			}
		}
	}
	
	@RequestMapping(value = "/queryDisSendAddress", method = { RequestMethod.POST })
	public OrderResponse queryDisSendAddress(@RequestBody OrderRequest ref){
		OrderResponse response = new OrderResponse();
		List<OrderDTO> list = orderService.queryDisSendAddress(ref.getTelephone());
		response.setOrders(list);
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}
	
	@RequestMapping(value = "/queryDisRecAddress", method = { RequestMethod.POST })
	public OrderResponse queryDisRecAddress(@RequestBody OrderRequest ref){
		OrderResponse response = new OrderResponse();
		List<OrderDTO> list = orderService.queryDisRecAddress(ref.getTelephone());
		response.setOrders(list);
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}
	
}

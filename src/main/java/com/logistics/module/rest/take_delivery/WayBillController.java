package com.logistics.module.rest.take_delivery;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.WayBillDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.OrderRequest;
import com.logistics.module.request.WayBillRequest;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.OrderService;
import com.logistics.module.service.WayBillService;
import com.logistics.module.service.WorkBillService;

import sms.util.MailUtils;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月7日 下午8:24:03
* 
*/
@RestController
@RequestMapping("/wayBill")
public class WayBillController {
	
	@Autowired
	WayBillService wayBillService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	WorkBillService workBillService;
	
	/**
	 * 下单
	 * @param ref
	 * @return
	 */
	@RequestMapping(value = "/placeAnOrder", method = { RequestMethod.POST })
	public BaseResponse placeAnOrder(@RequestBody OrderRequest ref) {
		BaseResponse response = new BaseResponse();
		//生成工单号
		String orderNum = UUID.randomUUID().toString().replaceAll("-", "");
		
		OrderDTO order = coverOrder(ref);
		order.setcOrderNum(orderNum);
		int num = orderService.insertSelective(order);
		if(num == 1){
			OrderDTO orderdto = orderService.queryByOrderNum(orderNum);
			WayBillDTO wayBill = new WayBillDTO();
			wayBill.setcActlweit(Double.valueOf(ref.getWeight()));
			wayBill.setcArriveCity(ref.getRecAddress());
			wayBill.setcDeltag("是");
			wayBill.setcFeeitemnum(1);
			wayBill.setcFloadreqr(ref.getPrice());
			wayBill.setcGoodsType(ref.getGoodsType());
			wayBill.setcNum(1);
			wayBill.setcPayTypeNum(ref.getPayTypeNum());
			wayBill.setcSendAddress(ref.getSendAddress());
			wayBill.setcSendAreaId(orderdto.getcSendAreaId());
			wayBill.setcSendCompany(orderdto.getcSendCompany());
			wayBill.setcSendMobile(orderdto.getcSendMobile());
			wayBill.setcSendName(orderdto.getcSendName());
			wayBill.setcRemark(orderdto.getcRemark());
			wayBill.setcSendProNum(orderdto.getcSendProNum());
			wayBill.setcSignStatus(2);
			wayBill.setcVol(ref.getVol());
			wayBill.setcWayBillNum(orderNum);
			wayBill.setcWayBillType("正常");
			wayBill.setcWeight(Double.valueOf(ref.getWeight()));
			wayBill.setcOrderId(orderdto.getcId());
			wayBill.setcRecAddress(orderdto.getcRecAddress());
			wayBill.setcRecAreaId(orderdto.getcRecAreaId());
			wayBill.setcRecCompany(orderdto.getcRecCompany());
			wayBill.setcRecMobile(orderdto.getcRecMobile());
			wayBill.setcRecName(orderdto.getcRecName());
			num = wayBillService.insertSelective(wayBill);
			if(num ==1){
				num = orderService.updateStatusById("2", orderdto.getcId());
			}
			
		}
		if(num == 1){
			response.setResult(ResponseCode.SUCCESS.getCode());
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		}else{
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
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
		order.setcOrderTime(new Date());
		order.setcStatus("1");
		order.setcOrderType("2");
		return order;
		
	}
	
	/**
	 * 下单
	 * @param ref
	 * @return
	 */
	@RequestMapping(value = "/mokeWayBill", method = { RequestMethod.POST })
	public BaseResponse mokeWayBill(@RequestBody WayBillRequest ref) {
		BaseResponse response = new BaseResponse();
		System.out.println(ref.toString());
		OrderDTO orderdto = orderService.selectByPrimaryKey(ref.getOrderId());
		WayBillDTO wayBill = new WayBillDTO();
		wayBill.setcActlweit(Double.valueOf(ref.getActlweit()));
		wayBill.setcArriveCity(ref.getcRecAddress());
		wayBill.setcDeltag("是");
		wayBill.setcFeeitemnum(Integer.valueOf(ref.getRealNum()));
		wayBill.setcFloadreqr(ref.getPrice());
		wayBill.setcGoodsType(ref.getcGoodsType());
		wayBill.setcNum(1);
		wayBill.setcPayTypeNum(ref.getcPayTypeNum());
		wayBill.setcSendAddress(ref.getcSendAddress());
		wayBill.setcSendAreaId(orderdto.getcSendAreaId());
		wayBill.setcSendCompany(ref.getcSendCompany());
		wayBill.setcSendMobile(ref.getcSendMobile());
		wayBill.setcSendName(ref.getcSendName());
		wayBill.setcRemark(orderdto.getcRemark());
		wayBill.setcSendProNum(ref.getcSendProNum());
		wayBill.setcSignStatus(2);
		wayBill.setcVol(ref.getVol());
		wayBill.setcWayBillNum(ref.getcOrderNum());
		wayBill.setcWayBillType("正常");
		wayBill.setcWeight(Double.valueOf(ref.getcWeight()));
		wayBill.setcOrderId(ref.getOrderId());
		wayBill.setcRecAddress(ref.getcRecAddress());
		wayBill.setcRecAreaId(orderdto.getcRecAreaId());
		wayBill.setcRecCompany(ref.getcRecCompany());
		wayBill.setcRecMobile(ref.getcRecMobile());
		wayBill.setcRecName(ref.getcRecName());
		int num = wayBillService.insertSelective(wayBill);
		if(num == 1){
			orderService.updateStatusById("2", orderdto.getcId());
			WorkBillDTO dto = workBillService.queryByOrderId(ref.getOrderId());
			if(dto != null){
				workBillService.updateState(dto.getcId());
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

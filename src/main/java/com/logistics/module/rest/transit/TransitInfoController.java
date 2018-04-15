package com.logistics.module.rest.transit;

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

import com.logistics.module.dto.DeliveryInfoDTO;
import com.logistics.module.dto.InOutStorageInfoDTO;
import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.SignInfoDTO;
import com.logistics.module.dto.TransitInfoDTO;
import com.logistics.module.dto.UserDTO;
import com.logistics.module.dto.WayBillDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.PageRequest;
import com.logistics.module.request.TransitInfoRequest;
import com.logistics.module.response.OrderProgressResponse;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.TransitInfoResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.DeliveryInfoService;
import com.logistics.module.service.InOutStorageInfoService;
import com.logistics.module.service.OrderService;
import com.logistics.module.service.SignInfoService;
import com.logistics.module.service.TransitInfoService;
import com.logistics.module.service.UserService;
import com.logistics.module.service.WayBillService;
import com.logistics.module.service.WorkBillService;
import com.logistics.module.util.TimeUtils;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月14日 下午9:12:37
* 
*/
@RestController
@RequestMapping("/transitInfo")
public class TransitInfoController {
	
	@Autowired
	TransitInfoService transitInfoService;
	
	@Autowired
	WayBillService wayBillService;
	
	@Autowired
	InOutStorageInfoService inOutStorageInfoService;
	
	@Autowired
	DeliveryInfoService deliveryInfoService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SignInfoService signInfoService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	WorkBillService workBillService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int transitInfoId =0;
		if(StringUtils.isEmpty(ref.getSearchStr())){
			transitInfoId = 0;
		}else{
			OrderDTO order = orderService.queryByOrderNum(ref.getSearchStr());
			if(order != null){
				WayBillDTO wayBill = wayBillService.queryByOrderId(order.getcId());
				if(wayBill!= null){
					TransitInfoDTO transit = transitInfoService.queryByWayBillId(wayBill.getcId());
					if(transit != null){
						transitInfoId = transit.getcId();
					}
				}
			}
		}
		
		int total = transitInfoService.queryTotal(transitInfoId);
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<TransitInfoDTO> rows1 = transitInfoService.queryByPage(transitInfoId, pageNum, ref.getRows());
		List<TransitInfoResponse> rows = new ArrayList<TransitInfoResponse>();
		if(!CollectionUtils.isEmpty(rows1)){
			for (TransitInfoDTO transitInfoDTO : rows1) {
				TransitInfoResponse row =new TransitInfoResponse();
				row.setTransitId(transitInfoDTO.getcId());
				row.setStatus(transitInfoDTO.getcStatus());
				row.setAddress(transitInfoDTO.getcOutletAddress());
				WayBillDTO wayBill = wayBillService.selectByPrimaryKey(transitInfoDTO.getcWaybillId());
				if(wayBill != null){
					row.setOrderNum(wayBill.getcWayBillNum());
					row.setSendName(wayBill.getcSendName());
					row.setSendMobile(wayBill.getcSendMobile());
					row.setSendAddress(wayBill.getcSendAddress());
					row.setRecName(wayBill.getcRecName());
					row.setRecMobile(wayBill.getcRecMobile());
					row.setRecAddress(wayBill.getcRecAddress());
					row.setGoodsType(wayBill.getcGoodsType());
				}
				StringBuffer str = new StringBuffer();
				//出入库信息
				List<InOutStorageInfoDTO> inOut = inOutStorageInfoService.queryByTransitInfoId(transitInfoDTO.getcId());
				if(!CollectionUtils.isEmpty(inOut)){
					for (InOutStorageInfoDTO inOutStorageInfoDTO : inOut) {
						str.append(inOutStorageInfoDTO.getcDescription()+"("+ inOutStorageInfoDTO.getcOperation()+")"+ "<br/>");
					}
				}
				//配送信息
				DeliveryInfoDTO deliverInfo = deliveryInfoService.selectByPrimaryKey(transitInfoDTO.getcDeliveryInfoId());
				if(deliverInfo != null){
					UserDTO user = userService.selectByPrimaryKey(Integer.valueOf(deliverInfo.getcCourierNum()));
					if(user != null){
						str.append("正在配送中：<br/>配送员："+user.getcUsername()+",电话："+user.getcTelephone()+"<br/>");
					}
				}
				//签收信息
				SignInfoDTO signInfo = signInfoService.selectByPrimaryKey(transitInfoDTO.getcSignInfoId());
				if(signInfo != null){
					str.append("已签收  签收人："+signInfo.getcSignName()+"<br/>");
				}
				row.setTransitInfo(str.toString());
				
				rows.add(row);
			}
		}
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}

	@RequestMapping(value = "/inOutStorage", method = { RequestMethod.POST })
	public BaseResponse inOutStorage(@RequestBody TransitInfoRequest ref){
		BaseResponse response = new BaseResponse();

		InOutStorageInfoDTO inOut = new InOutStorageInfoDTO();
		inOut.setcAddress(ref.getAddress());
		inOut.setcCreateTime(new Date());
		inOut.setcDescription(ref.getDescription());
		inOut.setcOperation(ref.getOperation());
		inOut.setcTransitInfoId(ref.getTransitInfoId());
		List<InOutStorageInfoDTO> inOuts = inOutStorageInfoService.queryByTransitInfoId(ref.getTransitInfoId());
		if(CollectionUtils.isEmpty(inOuts)){
			inOut.setcInOutIndex(0);
		}else{
			inOut.setcInOutIndex(inOuts.get(0).getcInOutIndex()+1);
		}
		int num = inOutStorageInfoService.insertSelective(inOut);
		if(num == 1){
			if("到达网点".equals(ref.getOperation())){
				TransitInfoDTO transit = new TransitInfoDTO();
				transit.setcId(ref.getTransitInfoId());
				transit.setcOutletAddress(ref.getAddress());
				transit.setcStatus(ref.getOperation());
				num = transitInfoService.updateByPrimaryKeySelective(transit);
			}
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
	
	@RequestMapping(value = "/delivery", method = { RequestMethod.POST })
	public BaseResponse delivery(@RequestBody TransitInfoRequest ref){
		BaseResponse response = new BaseResponse();

		int num = 0;
		UserDTO user = userService.selectByPrimaryKey(ref.getCourierNum());
		if(user == null){
			response.setErrorMsg("无该快递员，请重新指派");
			response.setResult(ResponseCode.FAILED.getCode());
		}else{
			if(!user.getcUsername().equals(ref.getCourierName())){
				response.setErrorMsg("快递员的姓名与工号不匹配，请确认后重新指派");
				response.setResult(ResponseCode.FAILED.getCode());
			}else{
				DeliveryInfoDTO deliver = new DeliveryInfoDTO();
				deliver.setcCourierNum(ref.getCourierNum()+"");
				deliver.setcCourierName(ref.getCourierName());
				deliver.setcDescription(ref.getDescription());
				num = deliveryInfoService.insertSelective(deliver);
				if(num > 0){
					TransitInfoDTO transit = new TransitInfoDTO();
					transit.setcId(ref.getTransitInfoId());
					transit.setcStatus("开始配送");
					transit.setcDeliveryInfoId(num);
					num = transitInfoService.updateByPrimaryKeySelective(transit);
					TransitInfoDTO dto = transitInfoService.selectByPrimaryKey(ref.getTransitInfoId());
					if(dto != null){
						num = wayBillService.updateSignStatus(dto.getcWaybillId(), 3);
						if(num > 0){
							response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
							response.setResult(ResponseCode.SUCCESS.getCode());
						}else{
							response.setErrorMsg("运单状态修改失败");
							response.setResult(ResponseCode.FAILED.getCode());
						}
					}else{
						response.setErrorMsg("运单信息丢失");
						response.setResult(ResponseCode.FAILED.getCode());
					}
				}else{
					response.setErrorMsg("指派失败");
					response.setResult(ResponseCode.FAILED.getCode());
				}
			}
		}
		return response;
	}
	
	@RequestMapping(value = "/sign", method = { RequestMethod.POST })
	public BaseResponse sign(@RequestBody TransitInfoRequest ref){
		BaseResponse response = new BaseResponse();
		SignInfoDTO signInfo = new SignInfoDTO();
		signInfo.setcDescription(ref.getDescription());
		signInfo.setcErrorRemark(ref.getErrorRemark());
		signInfo.setcSignName(ref.getSignName());
		signInfo.setcSignTime(ref.getSignTime());
		signInfo.setcSignType(ref.getSignType());
		int num = signInfoService.insertSelective(signInfo);
		if(num > 0){
			TransitInfoDTO transit = new TransitInfoDTO();
			transit.setcId(ref.getTransitInfoId());
			transit.setcStatus("正常签收");
			transit.setcSignInfoId(num);
			num = transitInfoService.updateByPrimaryKeySelective(transit);
			if(num > 0){
				TransitInfoDTO dto = transitInfoService.selectByPrimaryKey(ref.getTransitInfoId());
				if(dto != null){
					WayBillDTO wayBill = wayBillService.selectByPrimaryKey(dto.getcWaybillId());
					if(wayBill != null){
						num = wayBillService.updateSignStatus(wayBill.getcId(), 4);
						if(num > 0){
							response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
							response.setResult(ResponseCode.SUCCESS.getCode());
						}else{
							response.setErrorMsg("运单状态修改失败");
							response.setResult(ResponseCode.FAILED.getCode());
						}
						num = orderService.updateStatusById("3", wayBill.getcOrderId());
						if(num > 0){
							response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
							response.setResult(ResponseCode.SUCCESS.getCode());
						}else{
							response.setErrorMsg("订单状态修改失败");
							response.setResult(ResponseCode.FAILED.getCode());
						}
					}else{
						response.setErrorMsg("运单不存在");
						response.setResult(ResponseCode.FAILED.getCode());
					}
				}else{
					response.setErrorMsg("配送信息不存在");
					response.setResult(ResponseCode.FAILED.getCode());
				}
			}else{
				response.setErrorMsg("签收失败");
				response.setResult(ResponseCode.FAILED.getCode());
			}
		}
		return response;
	}
	
	@RequestMapping(value = "/searchProgress", method = { RequestMethod.POST })
	public BaseResponse searchProgress(@RequestBody OrderDTO ref){
		BaseResponse response = new BaseResponse();
		List<OrderProgressResponse> OrderProgressList = new ArrayList<OrderProgressResponse>();
		OrderDTO order = orderService.queryByOrderNum(ref.getcOrderNum());
		if(order != null){
			OrderProgressResponse progress1 = new OrderProgressResponse();
			progress1.setRecName("");
			progress1.setTransitInfo("订单生成成功");
			progress1.setTime(order.getcOrderTime());
			OrderProgressList.add(progress1);
			WayBillDTO wayBill = wayBillService.queryByOrderId(order.getcId());
			if(wayBill != null){
				OrderProgressResponse progress2 = new OrderProgressResponse();
				progress2.setRecName("");
				progress2.setTransitInfo("订单已打包发货，正在运输中");
				WorkBillDTO workBill = workBillService.queryByOrderId(order.getcId());
				if(workBill!= null){
					progress2.setTime(workBill.getcBuildtime());
				}
				OrderProgressList.add(progress2);
				TransitInfoDTO transitInfoDTO = transitInfoService.queryByWayBillId(wayBill.getcId());
				if(transitInfoDTO != null){
					//出入库信息
					List<InOutStorageInfoDTO> inOut = inOutStorageInfoService.queryByTransitInfoId(transitInfoDTO.getcId());
					Date time = new Date();
					if(!CollectionUtils.isEmpty(inOut)){
						for (InOutStorageInfoDTO inOutStorageInfoDTO : inOut) {
							OrderProgressResponse progress3 = new OrderProgressResponse();
							progress3.setRecName("");
							progress3.setTransitInfo(inOutStorageInfoDTO.getcDescription()+"("+ inOutStorageInfoDTO.getcOperation()+")");
							progress3.setTime(workBill.getcBuildtime());
							time = workBill.getcBuildtime();
							OrderProgressList.add(progress3);
						}
					}
					//配送信息
					DeliveryInfoDTO deliverInfo = deliveryInfoService.selectByPrimaryKey(transitInfoDTO.getcDeliveryInfoId());
					if(deliverInfo != null){
						UserDTO user = userService.selectByPrimaryKey(Integer.valueOf(deliverInfo.getcCourierNum()));
						if(user != null){
							OrderProgressResponse progress4 = new OrderProgressResponse();
							progress4.setRecName("");
							progress4.setTransitInfo("正在配送中：配送员："+user.getcUsername()+",电话："+user.getcTelephone());
							progress4.setTime(new Date(TimeUtils.addHour(time.getTime(),3)));
							OrderProgressList.add(progress4);
						}
					}
					//签收信息
					SignInfoDTO signInfo = signInfoService.selectByPrimaryKey(transitInfoDTO.getcSignInfoId());
					if(signInfo != null){
						OrderProgressResponse progress5 = new OrderProgressResponse();
						progress5.setRecName("");
						progress5.setTransitInfo("已签收  签收人："+signInfo.getcSignName());
						progress5.setTime(new Date(TimeUtils.addHour(time.getTime(),6)));
						OrderProgressList.add(progress5);
					}
				}
			}
			if(!CollectionUtils.isEmpty(OrderProgressList)){
				OrderProgressList.get(OrderProgressList.size()-1).setRecName(order.getcRecName());;
			}
			
		}
		response.setOrderProgress(OrderProgressList);
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}
}

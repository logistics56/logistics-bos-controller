package com.logistics.module.rest.take_delivery;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.WayBillDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.OrderRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.request.WayBillRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.OrderService;
import com.logistics.module.service.WayBillService;
import com.logistics.module.service.WorkBillService;
import com.logistics.module.util.POIUtil;

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
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = wayBillService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<WayBillDTO> rows = wayBillService.queryByPage( pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/fileImport", method = { RequestMethod.POST })
	public int fileImport(MultipartFile file) throws Exception {
		// 判断文件是否存在
		if (null == file) {
			return ResponseCode.FAILED.getCode();
		}

		List<String []> list = POIUtil.readExcel(file);
		for (String[] strings : list) {
			System.out.println(strings.length);
			if(Integer.valueOf(strings[0]) == 0){
						//生成工单号
						String orderNum = UUID.randomUUID().toString().replaceAll("-", "");
						OrderDTO order = new OrderDTO();
						order.setcSendName(strings[3]);
						order.setcSendMobile(strings[4]);
						order.setcSendCompany(strings[5]);
						order.setcSendAddress(strings[6]);
						order.setcRecName(strings[7]);
						order.setcRecMobile(strings[8]);
						order.setcRecCompany(strings[9]);
						order.setcRecAddress(strings[10]);
						order.setcGoodsType(strings[11]);
						order.setcWeight(Double.valueOf(strings[12]));
						order.setcSendProNum(strings[13]);
						order.setcPayTypeNum(strings[14]);
						order.setcRemark(strings[15]);
						order.setcOrderTime(new Date());
						order.setcOrderNum(orderNum);
						order.setcStatus("1");
						order.setcOrderType("2");
						order.setcOrderNum(orderNum);
						int num = orderService.insertSelective(order);
						if(num == 1){
							OrderDTO orderdto = orderService.queryByOrderNum(orderNum);
							WayBillDTO wayBill = new WayBillDTO();
							wayBill.setcActlweit(Double.valueOf(strings[12]));
							wayBill.setcArriveCity(strings[10]);
							wayBill.setcDeltag("是");
							wayBill.setcFeeitemnum(1);
							wayBill.setcFloadreqr(strings[1]);
							wayBill.setcGoodsType(strings[11]);
							wayBill.setcNum(1);
							wayBill.setcPayTypeNum(strings[14]);
							wayBill.setcSendAddress(strings[6]);
							wayBill.setcSendAreaId(orderdto.getcSendAreaId());
							wayBill.setcSendCompany(orderdto.getcSendCompany());
							wayBill.setcSendMobile(orderdto.getcSendMobile());
							wayBill.setcSendName(orderdto.getcSendName());
							wayBill.setcRemark(orderdto.getcRemark());
							wayBill.setcSendProNum(orderdto.getcSendProNum());
							wayBill.setcSignStatus(2);
							wayBill.setcVol(strings[2]);
							wayBill.setcWayBillNum(orderNum);
							wayBill.setcWayBillType("正常");
							wayBill.setcWeight(Double.valueOf(strings[12]));
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
			}else{
				OrderDTO orderdto = orderService.selectByPrimaryKey(Integer.valueOf(strings[0]));
				WayBillDTO wayBill = new WayBillDTO();
				wayBill.setcActlweit(Double.valueOf(orderdto.getcWeight()));
				wayBill.setcArriveCity(orderdto.getcRecAddress());
				wayBill.setcDeltag("是");
				wayBill.setcFeeitemnum(Integer.valueOf(1));
				wayBill.setcFloadreqr(strings[1]);
				wayBill.setcGoodsType(orderdto.getcGoodsType());
				wayBill.setcNum(1);
				wayBill.setcPayTypeNum(orderdto.getcPayTypeNum());
				wayBill.setcSendAddress(orderdto.getcSendAddress());
				wayBill.setcSendAreaId(orderdto.getcSendAreaId());
				wayBill.setcSendCompany(orderdto.getcSendCompany());
				wayBill.setcSendMobile(orderdto.getcSendMobile());
				wayBill.setcSendName(orderdto.getcSendName());
				wayBill.setcRemark(orderdto.getcRemark());
				wayBill.setcSendProNum(orderdto.getcSendProNum());
				wayBill.setcSignStatus(2);
				wayBill.setcVol(strings[2]);
				wayBill.setcWayBillNum(orderdto.getcOrderNum());
				wayBill.setcWayBillType("正常");
				wayBill.setcWeight(Double.valueOf(orderdto.getcWeight()));
				wayBill.setcOrderId(orderdto.getcId());
				wayBill.setcRecAddress(orderdto.getcRecAddress());
				wayBill.setcRecAreaId(orderdto.getcRecAreaId());
				wayBill.setcRecCompany(orderdto.getcRecCompany());
				wayBill.setcRecMobile(orderdto.getcRecMobile());
				wayBill.setcRecName(orderdto.getcRecName());
				int num = wayBillService.insertSelective(wayBill);
				if(num == 1){
					orderService.updateStatusById("2", orderdto.getcId());
					WorkBillDTO dto = workBillService.queryByOrderId(orderdto.getcId());
					if(dto != null){
						workBillService.updateState(dto.getcId());
					}
				}
			}
		}
		
		return ResponseCode.SUCCESS.getCode();
	}

}

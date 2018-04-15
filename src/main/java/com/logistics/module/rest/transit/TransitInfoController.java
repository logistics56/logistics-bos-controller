package com.logistics.module.rest.transit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.InOutStorageInfoDTO;
import com.logistics.module.dto.TransitInfoDTO;
import com.logistics.module.dto.WayBillDTO;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.TransitInfoResponse;
import com.logistics.module.service.InOutStorageInfoService;
import com.logistics.module.service.TransitInfoService;
import com.logistics.module.service.WayBillService;

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
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = transitInfoService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<TransitInfoDTO> rows1 = transitInfoService.queryByPage( pageNum, ref.getRows());
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
				List<InOutStorageInfoDTO> inOut = inOutStorageInfoService.queryByTransitInfoId(transitInfoDTO.getcId());
				if(!CollectionUtils.isEmpty(inOut)){
					StringBuffer str = new StringBuffer();
					for (InOutStorageInfoDTO inOutStorageInfoDTO : inOut) {
						str.append(inOutStorageInfoDTO.getcDescription()+"("+ inOutStorageInfoDTO.getcOperation()+")"+ "<br/>");
					}
					row.setTransitInfo(str.toString());
				}
				
				rows.add(row);
			}
		}
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}

}

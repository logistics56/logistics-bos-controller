package com.logistics.module.rest.base;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.crm.dto.CustomerDTO;
import com.logistics.module.crm.service.CustomerService;
import com.logistics.module.dto.FixedAreaDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.BaseRequest;
import com.logistics.module.request.CourierRequest;
import com.logistics.module.request.FixedAreaRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.FixedAreaService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月21日 下午10:09:28
* 
*/
@RestController
@RequestMapping("/fixedArea")
public class FixedAreaController {
	
	@Autowired
	FixedAreaService fixedAreaService;
	
	@Autowired
	CustomerService customerService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref) {
		PageResponse response = new PageResponse();
		if (StringUtils.isEmpty(ref.getSearchStr())) {
			int total = fixedAreaService.queryTotal();
			int pageNum = (ref.getPage() - 1) * ref.getRows();
			List<FixedAreaDTO> rows = fixedAreaService.queryByPage(pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		} else {
			int total = fixedAreaService.queryTotalByKeyword(ref.getSearchStr());
			int pageNum = (ref.getPage() - 1) * ref.getRows();
			List<FixedAreaDTO> rows = fixedAreaService.queryByKeyword(ref.getSearchStr(), pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		}

		return response;
	}

	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody FixedAreaRequest ref) {
		BaseResponse response = new BaseResponse();
		if (ref == null) {
			return response;
		}

		FixedAreaDTO rixedArea = new FixedAreaDTO();
		BeanUtils.copyProperties(ref, rixedArea);
		rixedArea.setcOperatingTime(new Date());
		int num =0;
		if ("add".equals(ref.getOperat())) {
			FixedAreaDTO dto = fixedAreaService.selectByPrimaryKey(ref.getcId());
			if(dto != null){
				response.setErrorMsg("操作失败，该编号已存在");
				response.setResult(ResponseCode.FAILED.getCode());
				return response;
			}
			num = fixedAreaService.insertSelective(rixedArea);
		} else {
			num = fixedAreaService.updateByPrimaryKeySelective(rixedArea);
		}
		if (num == 1) {
			response.setErrorMsg("操作成功");
			response.setResult(ResponseCode.SUCCESS.getCode());
		} else {
			response.setErrorMsg("操作失败");
			response.setResult(ResponseCode.FAILED.getCode());
		}
		return response;
	}

	@RequestMapping(value = "/findCustomers", method = { RequestMethod.POST })
	public List<CustomerDTO> findNoAssociationCustomers(@RequestBody BaseRequest ref) {
		BaseResponse response = new BaseResponse();
		List<CustomerDTO> list = customerService.queryByFixedAreaId(ref.getId());
		return list;
	}
	@RequestMapping(value = "/CustomersToFixedArea", method = { RequestMethod.POST })
	public BaseResponse CustomersToFixedArea(@RequestBody FixedAreaRequest ref) {
		BaseResponse response = new BaseResponse();
		for (String str : ref.getAssociationCustomerIds()) {
			int id = Integer.valueOf(str);
			customerService.updateFixedAreaId(ref.getCustomerFixedAreaId(), id);
		}
		for (String s : ref.getNoassociationCustomerIds()) {
			int id = Integer.valueOf(s);
			customerService.updateFixedAreaId("", id);
		}
		return response;
	}
}

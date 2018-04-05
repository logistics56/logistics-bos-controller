package com.logistics.module.rest.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.PermissionDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.DeleteIds;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.PermissionService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月5日 下午7:03:04
* 
*/
@RestController
@RequestMapping("/permission")
public class PermissionController {
	
	@Autowired
	PermissionService permissionService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = permissionService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<PermissionDTO> rows = permissionService.queryByPage( pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody PermissionDTO ref){
		BaseResponse response = new BaseResponse();
		int id = permissionService.queryMaxId() + 1;
		ref.setcId(id);
		int num = permissionService.insertSelective(ref);
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
	public List<PermissionDTO> queryAll(){
		
		List<PermissionDTO> rows = permissionService.queryAll();
		
		return rows;
	}
	
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			permissionService.deleteSelect(Integer.valueOf(str));
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}


}

package com.logistics.module.rest.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.MenuDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.DeleteIds;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.MenuService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月5日 下午5:28:30
* 
*/
@RestController
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	MenuService menuService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = menuService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<MenuDTO> rows = menuService.queryByPage( pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody MenuDTO ref){
		BaseResponse response = new BaseResponse();
		int id = menuService.queryMaxId() + 1;
		ref.setcId(id);
		int num = menuService.insertSelective(ref);
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
	public List<MenuDTO> queryAll(){
		
		List<MenuDTO> rows = menuService.queryAll();
		
		return rows;
	}
	
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			menuService.deleteSelect(Integer.valueOf(str));
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}

}

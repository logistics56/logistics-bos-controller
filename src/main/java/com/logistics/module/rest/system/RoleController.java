package com.logistics.module.rest.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.PermissionDTO;
import com.logistics.module.dto.RoleDTO;
import com.logistics.module.dto.RoleMenuDTO;
import com.logistics.module.dto.RolePermissionDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.DeleteIds;
import com.logistics.module.request.PageRequest;
import com.logistics.module.request.RoleRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.RoleMenuService;
import com.logistics.module.service.RolePermissionService;
import com.logistics.module.service.RoleService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月5日 下午7:46:03
* 
*/
@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	RoleService  roleService;
	
	@Autowired
	RolePermissionService rolePermissionService;
	
	@Autowired
	RoleMenuService roleMenuService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		
		int total = roleService.queryTotal();
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<RoleDTO> rows = roleService.queryByPage( pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody RoleRequest ref){
		BaseResponse response = new BaseResponse();
		int id = roleService.queryMaxId() + 1;
		RoleDTO role = new RoleDTO();
		role.setcId(id);
		role.setcDescription(ref.getcDescription());
		role.setcKeyword(ref.getcKeyword());
		role.setcName(ref.getcName());
		int num = roleService.insertSelective(role);
		if(num == 1){
			if(ref.getPermissionIds().length>0){
				for(String permission : ref.getPermissionIds()){
					RolePermissionDTO rolePermission = new RolePermissionDTO();
					rolePermission.setcPermissionId(Integer.valueOf(permission));
					rolePermission.setcRoleId(id);
					rolePermissionService.insertSelective(rolePermission);
				}
			}
			if(!StringUtils.isEmpty(ref.getMenuIds())){
				String[] menuIds = ref.getMenuIds().split(",");
				for (String menuId : menuIds) {
					RoleMenuDTO roleMenu = new RoleMenuDTO();
					roleMenu.setcMenuId(Integer.valueOf(menuId));
					roleMenu.setcRoleId(id);
					roleMenuService.insertSelective(roleMenu);
				}
			}
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
			response.setResult(ResponseCode.SUCCESS.getCode());
		}else{
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
			response.setResult(ResponseCode.FAILED.getCode());
		}
		return response;
	}
	
	@RequestMapping(value = "/queryAll", method = { RequestMethod.POST })
	public List<RoleDTO> queryAll(){
		
		List<RoleDTO> rows = roleService.queryAll();
		
		return rows;
	}
	
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			roleService.deleteSelect(Integer.valueOf(str));
			rolePermissionService.deleteByRoleId(Integer.valueOf(str));
			roleMenuService.deleteByRoleId(Integer.valueOf(str));
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}



}

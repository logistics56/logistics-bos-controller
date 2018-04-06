package com.logistics.module.rest.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.module.dto.PermissionDTO;
import com.logistics.module.dto.UserDTO;
import com.logistics.module.dto.UserRoleDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.DeleteIds;
import com.logistics.module.request.PageRequest;
import com.logistics.module.request.UserRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.UserRoleService;
import com.logistics.module.service.UserService;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月6日 上午10:56:18
* 
*/
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRoleService userRoleService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(UserRequest ref){
		PageResponse response = new PageResponse();
		if(ref.getUsernum() != null && !StringUtils.isEmpty(ref.getUsername())){
			int total = userService.queryTotalByIdorName(ref.getUsernum(), ref.getUsername());
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<UserDTO> rows = userService.queryByIdorName(ref.getUsernum(), ref.getUsername(), pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		}else{
			int total = userService.queryTotal();
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<UserDTO> rows = userService.queryByPage( pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		}
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody UserRequest ref){
		BaseResponse response = new BaseResponse();
		System.out.println(ref.toString());
		UserDTO user = new UserDTO();
		user.setcBirthday(ref.getBirthday());
		user.setcGender(ref.getGender());
		user.setcNickname(ref.getNickname());
		user.setcPassword(ref.getPassword());
		user.setcRemark(ref.getRemark());
		user.setcTelephone(ref.getTelephone());
		user.setcUsername(ref.getUsername());
		user.setcStation(ref.getStation());
		int num = userService.insertSelective(user);
		if(num > 0){
			if(ref.getRoleIds() != null){
				for (String roleId : ref.getRoleIds()) {
					UserRoleDTO userRole = new UserRoleDTO();
					userRole.setcRoleId(Integer.valueOf(roleId));
					userRole.setcUserId(num);
					userRoleService.insertSelective(userRole);
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
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			userService.deleteSelect(Integer.valueOf(str));
			userRoleService.deleteByUserId(Integer.valueOf(str));
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}
	
}

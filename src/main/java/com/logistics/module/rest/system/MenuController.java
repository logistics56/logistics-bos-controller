package com.logistics.module.rest.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
import com.logistics.module.service.RoleMenuService;

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
	
	@Autowired
	RoleMenuService roleMenuService;
	
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
	public List<ChildMenu> queryAll(){
		
		List<MenuDTO> rows = menuService.queryAll();
		
		if(CollectionUtils.isEmpty(rows)){
			return null;
		}
		
		List<ChildMenu> menuList = new ArrayList<ChildMenu>();
		List<ChildMenu> menuList1 = new ArrayList<ChildMenu>();
		
		for (MenuDTO menuDTO : rows) {
			if(menuDTO.getcPid()==null){
				ChildMenu menu = new ChildMenu();
				menu.setDescription(menuDTO.getcDescription());
				menu.setId(menuDTO.getcId());
				menu.setName(menuDTO.getcName());
				menu.setpId(null);
				menu.setPage(menuDTO.getcPage());
				menu.setParentMenu(null);
				menu.setPriority(menuDTO.getcPriority());
				menuList.add(menu);
			}
		}
		for (MenuDTO menuDTO : rows) {
			ChildMenu menu = new ChildMenu();
			if(menuDTO.getcPid()!=null){
				menu.setDescription(menuDTO.getcDescription());
				menu.setId(menuDTO.getcId());
				menu.setName(menuDTO.getcName());
				menu.setpId(menuDTO.getcPid());
				menu.setPage(menuDTO.getcPage());
				if(CollectionUtils.isEmpty(menuList)){
					menu.setParentMenu(null);
				}else{
					for(ChildMenu parent : menuList ){
						if(parent.getId() == menuDTO.getcId()){
							menu.setParentMenu(parent);
						}
					}
				}
				menu.setPriority(menuDTO.getcPriority());
			}else{
				menu.setDescription(menuDTO.getcDescription());
				menu.setId(menuDTO.getcId());
				menu.setName(menuDTO.getcName());
				menu.setpId(menuDTO.getcPid());
				menu.setPage(menuDTO.getcPage());
				menu.setParentMenu(null);
				menu.setPriority(menuDTO.getcPriority());
			}
			menuList1.add(menu);
		}
		
		return menuList1;
	}
	
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			menuService.deleteSelect(Integer.valueOf(str));
			roleMenuService.deleteByMenuId(Integer.valueOf(str));
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}

	@RequestMapping(value = "/selectAll", method = { RequestMethod.POST })
	public List<MenuDTO> selectAll(){
		
		List<MenuDTO> rows = menuService.queryAll();
		
		return rows;
	}
}


class ChildMenu{
	private String description;
	private Integer id;
	private String name;
	private Integer pId;
	private String page;
	private Object parentMenu;
	private Integer priority;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getpId() {
		return pId;
	}
	public void setpId(Integer pId) {
		this.pId = pId;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Object getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(Object parentMenu) {
		this.parentMenu = parentMenu;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
}

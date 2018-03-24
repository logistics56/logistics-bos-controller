package com.logistics.module.rest.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.logistics.module.dto.AreaDTO;
import com.logistics.module.dto.SubAreaDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.CourierRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.SubAreaResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.AreaService;
import com.logistics.module.service.SubAreaService;
import com.logistics.module.util.POIUtil;
import com.logistics.module.util.PinYin4jUtils;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月24日 下午12:58:52
* 
*/
@RestController
@RequestMapping("/subArea")
public class SubAreaController {
	
	@Autowired
	SubAreaService subAreaService;
	
	@Autowired
	AreaService areaService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref) {
		PageResponse response = new PageResponse();
		if (StringUtils.isEmpty(ref.getSearchStr())) {
			int total = subAreaService.queryTotal();
			int pageNum = (ref.getPage() - 1) * ref.getRows();
			List<SubAreaDTO> rows1 = subAreaService.queryByPage(pageNum, ref.getRows());
			List<SubAreaResponse> rows = new ArrayList<SubAreaResponse>();
			if(CollectionUtils.isEmpty(rows1)){
				return response;
			}
			for (SubAreaDTO r : rows1) {
				AreaDTO area = areaService.selectByPrimaryKey(r.getcAreaId());
				if(area == null){
					return response;
				}
				SubAreaResponse subAreaResponse = new SubAreaResponse();
				BeanUtils.copyProperties(r, subAreaResponse);
				subAreaResponse.setArea(area);
				rows.add(subAreaResponse);
			}
			response.setTotal(total);
			response.setRows(rows);
		} else {
			int total = subAreaService.queryTotalByKeyword(ref.getSearchStr());
			int pageNum = (ref.getPage() - 1) * ref.getRows();
			List<SubAreaDTO> rows1 = subAreaService.queryByKeyword(ref.getSearchStr(), pageNum, ref.getRows());
			List<SubAreaResponse> rows = new ArrayList<SubAreaResponse>();
			if(CollectionUtils.isEmpty(rows1)){
				return response;
			}
			for (SubAreaDTO r : rows1) {
				AreaDTO area = areaService.selectByPrimaryKey(r.getcAreaId());
				if(area == null){
					return response;
				}
				SubAreaResponse subAreaResponse = new SubAreaResponse();
				BeanUtils.copyProperties(r, subAreaResponse);
				subAreaResponse.setArea(area);
				rows.add(subAreaResponse);
			}
			response.setTotal(total);
			response.setRows(rows);
		}

		return response;
	}

	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody SubAreaDTO ref) {
		BaseResponse response = new BaseResponse();
		
		if (ref == null) {
			return response;
		}
		int num = 0;

		if (StringUtils.isEmpty(ref.getcId())) {
			List<SubAreaDTO> ids = subAreaService.queryMaxId();
			if (CollectionUtils.isEmpty(ids)) {
				ref.setcId(1 + "");
			} else {
				ref.setcId((Integer.valueOf(ids.get(0).getcId().trim()) + 1) + "");
			}

			num = subAreaService.insertSelective(ref);
		} else {
			num = subAreaService.updateByPrimaryKeySelective(ref);
		}
		if (num == 1) {
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
			response.setResult(ResponseCode.SUCCESS.getCode());
		} else {
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
			response.setResult(ResponseCode.FAILED.getCode());
		}
		return response;
	}

	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody CourierRequest ref) {
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			subAreaService.deleteSelect(str);
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
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
			if(strings.length<7){
				return ResponseCode.FAILED.getCode();
			}
				SubAreaDTO subArea = new SubAreaDTO();
				
				subArea.setcStartNum(strings[0]);
				subArea.setcEndnum(strings[1]);
				subArea.setcKeyWords(strings[2]);
				subArea.setcAssistKeyWords(strings[3]);
				subArea.setcSingle(strings[4]);
				subArea.setcAreaId(strings[5]);
				subArea.setcFixedareaId(strings[6]);
				
				List<SubAreaDTO> ids = subAreaService.queryMaxId();
				if(CollectionUtils.isEmpty(ids)){
					subArea.setcId(1+"");
				}else{
					subArea.setcId((Integer.valueOf(ids.get(0).getcId().trim()) + 1)+"");
				}
			
				subAreaService.insertSelective(subArea);
		}
		
		return ResponseCode.SUCCESS.getCode();
	}

}

package com.logistics.module.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.CourierRequest;
import com.logistics.module.request.FileImportRequest;
import com.logistics.module.request.PageRequest;
import com.logistics.module.response.CourierResponse;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.AreaService;
import com.logistics.module.util.PinYin4jUtils;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年3月18日 上午11:06:08
* 
*/
@RestController
@RequestMapping("/area")
public class AreaController {
	
	@Autowired
	AreaService areaService;
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref){
		PageResponse response = new PageResponse();
		if(StringUtils.isEmpty(ref.getSearchStr())){
			int total = areaService.queryTotal();
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<AreaDTO> rows = areaService.queryByPage( pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		}else{
			int total = areaService.queryTotalByKeyword(ref.getSearchStr());
			int pageNum = (ref.getPage()-1) * ref.getRows();
			List<AreaDTO> rows = areaService.queryByKeyword(ref.getSearchStr(), pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		}
		
		
		return response;
	}
	
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody AreaDTO ref){
		BaseResponse response = new BaseResponse();
		if(ref == null){
			return response;
		}
		int num= 0;
		// 基于pinyin4j生成城市编码和简码
		String province = ref.getcProvince();
		String city = ref.getcCity();
		String district = ref.getcDistrict();
		province = province.substring(0, province.length() - 1);
		city = city.substring(0, city.length() - 1);
		district = district.substring(0, district.length() - 1);
		// 简码
		String[] headArray = PinYin4jUtils.getHeadByString(province + city
				+ district);
		StringBuffer buffer = new StringBuffer();
		for (String headStr : headArray) {
			buffer.append(headStr);
		}
		String shortcode = buffer.toString();
		ref.setcShortcode(shortcode);
		// 城市编码
		String citycode = PinYin4jUtils.hanziToPinyin(city, "");
		ref.setcCitycode(citycode);
		
		if(StringUtils.isEmpty(ref.getcId())){
			List<AreaDTO> ids = areaService.queryMaxId();
			if(CollectionUtils.isEmpty(ids)){
				ref.setcId(1+"");
			}else{
				ref.setcId((Integer.valueOf(ids.get(0).getcId().trim()) + 1)+"");
			}
			
			num = areaService.insertSelective(ref);
		}else{
			num = areaService.updateByPrimaryKeySelective(ref);
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
	@RequestMapping(value = "/fileImport", method = { RequestMethod.POST })
	public BaseResponse fileImport(MultipartFile file) throws Exception{
		BaseResponse response = new BaseResponse();
		
		System.out.println("12346");
		
		
//		System.out.println("文件名="+ref.getFileFileName());
//		System.out.println("文件类型="+ref.getFileContentType());
		
//		Workbook workbook = null;
//		Sheet sheet = null;
//		if(ref.getFileFileName().endsWith(".xls")) {
//			workbook = new HSSFWorkbook(new FileInputStream(ref.getFile()));
//		} else if(ref.getFileFileName().endsWith(".xlsx")) {
//			workbook = new XSSFWorkbook(new FileInputStream(ref.getFile()));
//		}
//		
//		if(workbook !=null ) {
//			sheet = workbook.getSheetAt(0);
//		}
//		
		// 编写解析代码逻辑
		// 基于.xls 格式解析 HSSF
		// 1、 加载Excel文件对象
//		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
//		// 2、 读取一个sheet
//		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
//		// 3、 读取sheet中每一行
//		if(sheet != null) {
//			for (Row row : sheet) {
//				// 一行数据 对应 一个区域对象
//				if (row.getRowNum() == 0) {
//					// 第一行 跳过
//					continue;
//				}
//				// 跳过空行
//				if (row.getCell(0) == null
//						|| org.apache.commons.lang.StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
//					continue;
//				}
//				System.out.println();
//				System.out.println("第一例："+row.getCell(0).getStringCellValue()+"第二例："+row.getCell(1).getStringCellValue()+"第三例："+row.getCell(2).getStringCellValue()+"第四例："+row.getCell(3).getStringCellValue());

				//				AreaDTO area = new AreaDTO();
//				
//				area.setcProvince(row.getCell(0).getStringCellValue());
//				area.setcCity(row.getCell(1).getStringCellValue());
//				area.setcDistrict(row.getCell(2).getStringCellValue());
//				area.setcPostcode(row.getCell(3).getStringCellValue());
//				
//				List<AreaDTO> ids = areaService.queryMaxId();
//				if(CollectionUtils.isEmpty(ids)){
//					area.setcId(1+"");
//				}else{
//					area.setcId((Integer.valueOf(ids.get(0).getcId().trim()) + 1)+"");
//				}
//				// 基于pinyin4j生成城市编码和简码
//				String province = area.getcProvince();
//				String city = area.getcCity();
//				String district = area.getcDistrict();
//				province = province.substring(0, province.length() - 1);
//				city = city.substring(0, city.length() - 1);
//				district = district.substring(0, district.length() - 1);
//				// 简码
//				String[] headArray = PinYin4jUtils.getHeadByString(province + city
//						+ district);
//				StringBuffer buffer = new StringBuffer();
//				for (String headStr : headArray) {
//					buffer.append(headStr);
//				}
//				String shortcode = buffer.toString();
//				area.setcShortcode(shortcode);
//				// 城市编码
//				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
//				area.setcCitycode(citycode);
	
//			}
//		}
		
		
		
		
		
		
		
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}
	
	@RequestMapping(value = "/deleteData", method = { RequestMethod.POST })
	public BaseResponse deleteData(@RequestBody CourierRequest ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			areaService.deleteSelect(str);
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}

}

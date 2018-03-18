package com.logistics.module.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
import com.logistics.module.util.POIUtil;
import com.logistics.module.util.PinYin4jUtils;

/**
 *
 * @author 李振 E-mail:lizhn95@163.com
 * @version 创建时间：2018年3月18日 上午11:06:08
 * 
 */
@RestController
@RequestMapping("/area")
public class AreaController {

	@Autowired
	AreaService areaService;

	private static Logger logger = Logger.getLogger(AreaController.class);
	private final static String xls = "xls";
	private final static String xlsx = "xlsx";

	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(PageRequest ref) {
		PageResponse response = new PageResponse();
		if (StringUtils.isEmpty(ref.getSearchStr())) {
			int total = areaService.queryTotal();
			int pageNum = (ref.getPage() - 1) * ref.getRows();
			List<AreaDTO> rows = areaService.queryByPage(pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		} else {
			int total = areaService.queryTotalByKeyword(ref.getSearchStr());
			int pageNum = (ref.getPage() - 1) * ref.getRows();
			List<AreaDTO> rows = areaService.queryByKeyword(ref.getSearchStr(), pageNum, ref.getRows());
			response.setTotal(total);
			response.setRows(rows);
		}

		return response;
	}

	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public BaseResponse saveData(@RequestBody AreaDTO ref) {
		BaseResponse response = new BaseResponse();
		if (ref == null) {
			return response;
		}
		int num = 0;
		// 基于pinyin4j生成城市编码和简码
		String province = ref.getcProvince();
		String city = ref.getcCity();
		String district = ref.getcDistrict();
		province = province.substring(0, province.length() - 1);
		city = city.substring(0, city.length() - 1);
		district = district.substring(0, district.length() - 1);
		// 简码
		String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
		StringBuffer buffer = new StringBuffer();
		for (String headStr : headArray) {
			buffer.append(headStr);
		}
		String shortcode = buffer.toString();
		ref.setcShortcode(shortcode);
		// 城市编码
		String citycode = PinYin4jUtils.hanziToPinyin(city, "");
		ref.setcCitycode(citycode);

		if (StringUtils.isEmpty(ref.getcId())) {
			List<AreaDTO> ids = areaService.queryMaxId();
			if (CollectionUtils.isEmpty(ids)) {
				ref.setcId(1 + "");
			} else {
				ref.setcId((Integer.valueOf(ids.get(0).getcId().trim()) + 1) + "");
			}

			num = areaService.insertSelective(ref);
		} else {
			num = areaService.updateByPrimaryKeySelective(ref);
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
			areaService.deleteSelect(str);
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		return response;
	}

	@RequestMapping(value = "/fileImport", method = { RequestMethod.POST })
	public int fileImport(MultipartFile file) throws Exception {
		BaseResponse response = new BaseResponse();

		// 判断文件是否存在
		if (null == file) {
			return ResponseCode.FAILED.getCode();
		}

		List<String []> list = POIUtil.readExcel(file);
		for (String[] strings : list) {
			if(strings.length<4){
				return ResponseCode.FAILED.getCode();
			}
				AreaDTO area = new AreaDTO();
				
				area.setcProvince(strings[0]);
				area.setcCity(strings[1]);
				area.setcDistrict(strings[2]);
				area.setcPostcode(strings[3]);
				
				List<AreaDTO> ids = areaService.queryMaxId();
				if(CollectionUtils.isEmpty(ids)){
					area.setcId(1+"");
				}else{
					area.setcId((Integer.valueOf(ids.get(0).getcId().trim()) + 1)+"");
				}
				// 基于pinyin4j生成城市编码和简码
				String province = area.getcProvince();
				String city = area.getcCity();
				String district = area.getcDistrict();
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
				area.setcShortcode(shortcode);
				// 城市编码
				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
				area.setcCitycode(citycode);
				areaService.insertSelective(area);
		}
		
		return ResponseCode.SUCCESS.getCode();
	}


}

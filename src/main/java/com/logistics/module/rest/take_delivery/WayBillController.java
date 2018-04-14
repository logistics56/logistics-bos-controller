package com.logistics.module.rest.take_delivery;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.logistics.module.dto.OrderDTO;
import com.logistics.module.dto.TransitInfoDTO;
import com.logistics.module.dto.WayBillDTO;
import com.logistics.module.dto.WorkBillDTO;
import com.logistics.module.enums.ResponseCode;
import com.logistics.module.request.DeleteIds;
import com.logistics.module.request.OrderRequest;
import com.logistics.module.request.WayBillRequest;
import com.logistics.module.request.WayBillSearchRequest;
import com.logistics.module.response.PageResponse;
import com.logistics.module.response.base.BaseResponse;
import com.logistics.module.service.OrderService;
import com.logistics.module.service.TransitInfoService;
import com.logistics.module.service.WayBillService;
import com.logistics.module.service.WorkBillService;
import com.logistics.module.util.FileUtils;
import com.logistics.module.util.POIUtil;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
*
* @author 李振        E-mail:lizhn95@163.com
* @version 创建时间：2018年4月7日 下午8:24:03
* 
*/
@RestController
@RequestMapping("/wayBill")
public class WayBillController {
	
	@Autowired
	WayBillService wayBillService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	WorkBillService workBillService;
	
	@Autowired
	TransitInfoService transitInfoService;
	
	/**
	 * 下单
	 * @param ref
	 * @return
	 */
	@RequestMapping(value = "/placeAnOrder", method = { RequestMethod.POST })
	public BaseResponse placeAnOrder(@RequestBody OrderRequest ref) {
		BaseResponse response = new BaseResponse();
		//生成工单号
		String orderNum = UUID.randomUUID().toString().replaceAll("-", "");
		
		OrderDTO order = coverOrder(ref);
		order.setcOrderNum(orderNum);
		int num = orderService.insertSelective(order);
		if(num == 1){
			OrderDTO orderdto = orderService.queryByOrderNum(orderNum);
			WayBillDTO wayBill = new WayBillDTO();
			wayBill.setcActlweit(Double.valueOf(ref.getWeight()));
			wayBill.setcArriveCity(ref.getRecAddress());
			wayBill.setcDeltag("是");
			wayBill.setcFeeitemnum(1);
			wayBill.setcFloadreqr(ref.getPrice());
			wayBill.setcGoodsType(ref.getGoodsType());
			wayBill.setcNum(1);
			wayBill.setcPayTypeNum(ref.getPayTypeNum());
			wayBill.setcSendAddress(ref.getSendAddress());
			wayBill.setcSendAreaId(orderdto.getcSendAreaId());
			wayBill.setcSendCompany(orderdto.getcSendCompany());
			wayBill.setcSendMobile(orderdto.getcSendMobile());
			wayBill.setcSendName(orderdto.getcSendName());
			wayBill.setcRemark(orderdto.getcRemark());
			wayBill.setcSendProNum(orderdto.getcSendProNum());
			wayBill.setcSignStatus(1);
			wayBill.setcVol(ref.getVol());
			wayBill.setcWayBillNum(orderNum);
			wayBill.setcWayBillType("正常");
			wayBill.setcWeight(Double.valueOf(ref.getWeight()));
			wayBill.setcOrderId(orderdto.getcId());
			wayBill.setcRecAddress(orderdto.getcRecAddress());
			wayBill.setcRecAreaId(orderdto.getcRecAreaId());
			wayBill.setcRecCompany(orderdto.getcRecCompany());
			wayBill.setcRecMobile(orderdto.getcRecMobile());
			wayBill.setcRecName(orderdto.getcRecName());
			num = wayBillService.insertSelective(wayBill);
			if(num ==1){
				num = orderService.updateStatusById("2", orderdto.getcId());
			}
			
		}
		if(num == 1){
			response.setResult(ResponseCode.SUCCESS.getCode());
			response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		}else{
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg(ResponseCode.FAILED.getMsg());
		}
		return response;
	}
	
	private OrderDTO coverOrder(OrderRequest ref){
		OrderDTO order = new OrderDTO();
		order.setcSendName(ref.getSendName());
		order.setcSendMobile(ref.getSendMobile());
		order.setcSendCompany(ref.getSendCompany());
		order.setcSendAddress(ref.getSendAddress());
		order.setcRecName(ref.getRecName());
		order.setcRecMobile(ref.getRecMobile());
		order.setcRecCompany(ref.getRecCompany());
		order.setcRecAddress(ref.getRecAddress());
		order.setcGoodsType(ref.getGoodsType());
		order.setcWeight(Double.valueOf(ref.getWeight()));
		order.setcRemark(ref.getRemark());
		order.setcSendProNum(ref.getSendProNum());
		order.setcPayTypeNum(ref.getPayTypeNum());
		order.setcOrderTime(new Date());
		order.setcStatus("1");
		order.setcOrderType("2");
		return order;
		
	}
	
	/**
	 * 快递员录入运单
	 * @param ref
	 * @return
	 */
	@RequestMapping(value = "/mokeWayBill", method = { RequestMethod.POST })
	public BaseResponse mokeWayBill(@RequestBody WayBillRequest ref) {
		BaseResponse response = new BaseResponse();
		WayBillDTO result = wayBillService.queryByOrderId(ref.getOrderId());
		if(result != null){
			response.setResult(ResponseCode.FAILED.getCode());
			response.setErrorMsg("该订单已录入，请勿重复录入");
		}else{
			OrderDTO orderdto = orderService.selectByPrimaryKey(ref.getOrderId());
			WayBillDTO wayBill = new WayBillDTO();
			wayBill.setcActlweit(Double.valueOf(ref.getActlweit()));
			wayBill.setcArriveCity(ref.getcRecAddress());
			wayBill.setcDeltag("是");
			wayBill.setcFeeitemnum(Integer.valueOf(ref.getRealNum()));
			wayBill.setcFloadreqr(ref.getPrice());
			wayBill.setcGoodsType(ref.getcGoodsType());
			wayBill.setcNum(1);
			wayBill.setcPayTypeNum(ref.getcPayTypeNum());
			wayBill.setcSendAddress(ref.getcSendAddress());
			wayBill.setcSendAreaId(orderdto.getcSendAreaId());
			wayBill.setcSendCompany(ref.getcSendCompany());
			wayBill.setcSendMobile(ref.getcSendMobile());
			wayBill.setcSendName(ref.getcSendName());
			wayBill.setcRemark(orderdto.getcRemark());
			wayBill.setcSendProNum(ref.getcSendProNum());
			wayBill.setcSignStatus(1);
			wayBill.setcVol(ref.getVol());
			wayBill.setcWayBillNum(ref.getcOrderNum());
			wayBill.setcWayBillType("正常");
			wayBill.setcWeight(Double.valueOf(ref.getcWeight()));
			wayBill.setcOrderId(ref.getOrderId());
			wayBill.setcRecAddress(ref.getcRecAddress());
			wayBill.setcRecAreaId(orderdto.getcRecAreaId());
			wayBill.setcRecCompany(ref.getcRecCompany());
			wayBill.setcRecMobile(ref.getcRecMobile());
			wayBill.setcRecName(ref.getcRecName());
			int num = wayBillService.insertSelective(wayBill);
			if(num == 1){
				orderService.updateStatusById("2", orderdto.getcId());
				WorkBillDTO dto = workBillService.queryByOrderId(ref.getOrderId());
				if(dto != null){
					workBillService.updateState(dto.getcId());
				}
				response.setResult(ResponseCode.SUCCESS.getCode());
				response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
			}else{
				response.setResult(ResponseCode.FAILED.getCode());
				response.setErrorMsg("系统繁忙！请稍后重试");
			}
		}
		return response;
	}
	
	@RequestMapping(value = "/queryPageData", method = { RequestMethod.POST })
	public PageResponse queryPageData(WayBillSearchRequest ref){
		PageResponse response = new PageResponse();
		String orderNum;
		String sendAddress;
		String recAddress;
		String sendProNum;
		Integer signStatus;
		if(StringUtils.isEmpty(ref.getOrderNum())){
			orderNum = null;
		}else{
			orderNum = ref.getOrderNum();
		}
		if(StringUtils.isEmpty(ref.getSendAddress())){
			sendAddress = null;
		}else{
			sendAddress = ref.getSendAddress();
		}
		if(StringUtils.isEmpty(ref.getRecAddress())){
			recAddress = null;
		}else{
			recAddress = ref.getRecAddress();
		}
		if(StringUtils.isEmpty(ref.getSendProNum())){
			sendProNum = null;
		}else{
			sendProNum = ref.getSendProNum();
		}
		if(StringUtils.isEmpty(ref.getSignStatus())){
			signStatus = 0;
		}else{
			signStatus = Integer.valueOf(ref.getSignStatus());
		}
		int total = wayBillService.queryTotal(orderNum, sendAddress, recAddress, sendProNum, signStatus);
		int pageNum = (ref.getPage()-1) * ref.getRows();
		List<WayBillDTO> rows = wayBillService.queryByPage(orderNum, sendAddress, recAddress, sendProNum, signStatus, pageNum, ref.getRows());
		response.setTotal(total);
		response.setRows(rows);
		
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
			System.out.println(strings.length);
			if(Integer.valueOf(strings[0]) == 0){
						//生成工单号
						String orderNum = UUID.randomUUID().toString().replaceAll("-", "");
						OrderDTO order = new OrderDTO();
						order.setcSendName(strings[3]);
						order.setcSendMobile(strings[4]);
						order.setcSendCompany(strings[5]);
						order.setcSendAddress(strings[6]);
						order.setcRecName(strings[7]);
						order.setcRecMobile(strings[8]);
						order.setcRecCompany(strings[9]);
						order.setcRecAddress(strings[10]);
						order.setcGoodsType(strings[11]);
						order.setcWeight(Double.valueOf(strings[12]));
						order.setcSendProNum(strings[13]);
						order.setcPayTypeNum(strings[14]);
						order.setcRemark(strings[15]);
						order.setcOrderTime(new Date());
						order.setcOrderNum(orderNum);
						order.setcStatus("1");
						order.setcOrderType("2");
						order.setcOrderNum(orderNum);
						int num = orderService.insertSelective(order);
						if(num == 1){
							OrderDTO orderdto = orderService.queryByOrderNum(orderNum);
							WayBillDTO wayBill = new WayBillDTO();
							wayBill.setcActlweit(Double.valueOf(strings[12]));
							wayBill.setcArriveCity(strings[10]);
							wayBill.setcDeltag("是");
							wayBill.setcFeeitemnum(1);
							wayBill.setcFloadreqr(strings[1]);
							wayBill.setcGoodsType(strings[11]);
							wayBill.setcNum(1);
							wayBill.setcPayTypeNum(strings[14]);
							wayBill.setcSendAddress(strings[6]);
							wayBill.setcSendAreaId(orderdto.getcSendAreaId());
							wayBill.setcSendCompany(orderdto.getcSendCompany());
							wayBill.setcSendMobile(orderdto.getcSendMobile());
							wayBill.setcSendName(orderdto.getcSendName());
							wayBill.setcRemark(orderdto.getcRemark());
							wayBill.setcSendProNum(orderdto.getcSendProNum());
							wayBill.setcSignStatus(1);
							wayBill.setcVol(strings[2]);
							wayBill.setcWayBillNum(orderNum);
							wayBill.setcWayBillType("正常");
							wayBill.setcWeight(Double.valueOf(strings[12]));
							wayBill.setcOrderId(orderdto.getcId());
							wayBill.setcRecAddress(orderdto.getcRecAddress());
							wayBill.setcRecAreaId(orderdto.getcRecAreaId());
							wayBill.setcRecCompany(orderdto.getcRecCompany());
							wayBill.setcRecMobile(orderdto.getcRecMobile());
							wayBill.setcRecName(orderdto.getcRecName());
							num = wayBillService.insertSelective(wayBill);
							if(num ==1){
								num = orderService.updateStatusById("2", orderdto.getcId());
							}
					}
			}else{
				WayBillDTO result = wayBillService.queryByOrderId(Integer.valueOf(strings[0]));
				if(result == null){
					OrderDTO orderdto = orderService.selectByPrimaryKey(Integer.valueOf(strings[0]));
					WayBillDTO wayBill = new WayBillDTO();
					wayBill.setcActlweit(Double.valueOf(orderdto.getcWeight()));
					wayBill.setcArriveCity(orderdto.getcRecAddress());
					wayBill.setcDeltag("是");
					wayBill.setcFeeitemnum(Integer.valueOf(1));
					wayBill.setcFloadreqr(strings[1]);
					wayBill.setcGoodsType(orderdto.getcGoodsType());
					wayBill.setcNum(1);
					wayBill.setcPayTypeNum(orderdto.getcPayTypeNum());
					wayBill.setcSendAddress(orderdto.getcSendAddress());
					wayBill.setcSendAreaId(orderdto.getcSendAreaId());
					wayBill.setcSendCompany(orderdto.getcSendCompany());
					wayBill.setcSendMobile(orderdto.getcSendMobile());
					wayBill.setcSendName(orderdto.getcSendName());
					wayBill.setcRemark(orderdto.getcRemark());
					wayBill.setcSendProNum(orderdto.getcSendProNum());
					wayBill.setcSignStatus(1);
					wayBill.setcVol(strings[2]);
					wayBill.setcWayBillNum(orderdto.getcOrderNum());
					wayBill.setcWayBillType("正常");
					wayBill.setcWeight(Double.valueOf(orderdto.getcWeight()));
					wayBill.setcOrderId(orderdto.getcId());
					wayBill.setcRecAddress(orderdto.getcRecAddress());
					wayBill.setcRecAreaId(orderdto.getcRecAreaId());
					wayBill.setcRecCompany(orderdto.getcRecCompany());
					wayBill.setcRecMobile(orderdto.getcRecMobile());
					wayBill.setcRecName(orderdto.getcRecName());
					int num = wayBillService.insertSelective(wayBill);
					if(num == 1){
						orderService.updateStatusById("2", orderdto.getcId());
						WorkBillDTO dto = workBillService.queryByOrderId(orderdto.getcId());
						if(dto != null){
							workBillService.updateState(dto.getcId());
						}
					}
				}
			}
		}
		
		return ResponseCode.SUCCESS.getCode();
	}
	
	//中转配送
	@RequestMapping(value = "/transit", method = { RequestMethod.POST })
	public BaseResponse transit(@RequestBody DeleteIds ref){
		BaseResponse response = new BaseResponse();
		String ids = ref.getIds();
		String[] idArra = ids.split(",");
		for (String str : idArra) {
			TransitInfoDTO transitInfoDTO = transitInfoService.queryByWayBillId(Integer.valueOf(str));
			if(transitInfoDTO == null){
				//插入
				TransitInfoDTO transitInfo = new TransitInfoDTO();
				transitInfo.setcStatus("出入库中转");
				transitInfo.setcWaybillId(Integer.valueOf(str));
				
				int num = transitInfoService.insertSelective(transitInfo);
				//更改状态（运单）
				if(num == 1){
					wayBillService.updateSignStatus(Integer.valueOf(str), 2);
				}
			}
		}
		response.setErrorMsg(ResponseCode.SUCCESS.getMsg());
		response.setResult(ResponseCode.SUCCESS.getCode());
		
		return response;
	}
	
	@RequestMapping(value = "/exportXls", method = { RequestMethod.POST })
	public void exportXls(WayBillSearchRequest ref, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String orderNum;
		String sendAddress;
		String recAddress;
		String sendProNum;
		Integer signStatus;
		if(StringUtils.isEmpty(ref.getOrderNum())){
			orderNum = null;
		}else{
			orderNum = ref.getOrderNum();
		}
		if(StringUtils.isEmpty(ref.getSendAddress())){
			sendAddress = null;
		}else{
			sendAddress = ref.getSendAddress();
		}
		if(StringUtils.isEmpty(ref.getRecAddress())){
			recAddress = null;
		}else{
			recAddress = ref.getRecAddress();
		}
		if(StringUtils.isEmpty(ref.getSendProNum())){
			sendProNum = null;
		}else{
			sendProNum = ref.getSendProNum();
		}
		if(StringUtils.isEmpty(ref.getSignStatus())){
			signStatus = 0;
		}else{
			signStatus = Integer.valueOf(ref.getSignStatus());
		}
		// 查询出 满足当前条件 结果数据
		List<WayBillDTO> wayBills = wayBillService.findWayBills(orderNum, sendAddress, recAddress, sendProNum, signStatus);
		// 生成Excel文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("运单数据");
		// 表头
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("运单号");
		headRow.createCell(1).setCellValue("寄件人");
		headRow.createCell(2).setCellValue("寄件人电话");
		headRow.createCell(3).setCellValue("寄件人地址");
		headRow.createCell(4).setCellValue("收件人");
		headRow.createCell(5).setCellValue("收件人电话");
		headRow.createCell(6).setCellValue("收件人地址");
		// 表格数据
		for (WayBillDTO wayBill : wayBills) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(wayBill.getcWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getcSendName());
			dataRow.createCell(2).setCellValue(wayBill.getcSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getcSendAddress());
			dataRow.createCell(4).setCellValue(wayBill.getcRecName());
			dataRow.createCell(5).setCellValue(wayBill.getcRecMobile());
			dataRow.createCell(6).setCellValue(wayBill.getcRecAddress());
		}

		// 下载导出
		// 设置头信息
		response.setContentType("application/vnd.ms-excel");
		String filename = "运单数据.xls";
		String agent = request.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		response.setHeader("Content-Disposition","attachment;filename=" + filename);

		ServletOutputStream outputStream = response.getOutputStream();
		hssfWorkbook.write(outputStream);

		// 关闭
		hssfWorkbook.close();
	}
	
	@RequestMapping(value = "/exportPdf", method = { RequestMethod.POST })
	public void exportPdf(WayBillSearchRequest ref, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
		String orderNum;
		String sendAddress;
		String recAddress;
		String sendProNum;
		Integer signStatus;
		if(StringUtils.isEmpty(ref.getOrderNum())){
			orderNum = null;
		}else{
			orderNum = ref.getOrderNum();
		}
		if(StringUtils.isEmpty(ref.getSendAddress())){
			sendAddress = null;
		}else{
			sendAddress = ref.getSendAddress();
		}
		if(StringUtils.isEmpty(ref.getRecAddress())){
			recAddress = null;
		}else{
			recAddress = ref.getRecAddress();
		}
		if(StringUtils.isEmpty(ref.getSendProNum())){
			sendProNum = null;
		}else{
			sendProNum = ref.getSendProNum();
		}
		if(StringUtils.isEmpty(ref.getSignStatus())){
			signStatus = 0;
		}else{
			signStatus = Integer.valueOf(ref.getSignStatus());
		}
		// 查询出 满足当前条件 结果数据
		List<WayBillDTO> wayBills = wayBillService.findWayBills(orderNum, sendAddress, recAddress, sendProNum, signStatus);
		// 下载导出
		// 设置头信息
		response.setContentType("application/pdf");
		String filename = "运单数据.pdf";
		String agent = request.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		response.setHeader("Content-Disposition","attachment;filename=" + filename);

		// 生成PDF文件
		Document document = new Document();
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		// 写PDF数据
		// 向document 生成pdf表格
		Table table = new Table(7);
		table.setWidth(90); // 宽度
		table.setBorder(1); // 边框
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式

		// 设置表格字体
		BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
				false);
		Font font1 = new Font(cn, 10, Font.NORMAL, Color.RED);
		Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);

		// 写表头
		table.addCell(buildCell("运单号", font1));
		table.addCell(buildCell("寄件人", font1));
		table.addCell(buildCell("寄件人电话", font1));
		table.addCell(buildCell("寄件人地址", font1));
		table.addCell(buildCell("收件人", font1));
		table.addCell(buildCell("收件人电话", font1));
		table.addCell(buildCell("收件人地址", font1));
		// 写数据
		for (WayBillDTO wayBill : wayBills) {
			table.addCell(buildCell(wayBill.getcWayBillNum(), font));
			table.addCell(buildCell(wayBill.getcSendName(), font));
			table.addCell(buildCell(wayBill.getcSendMobile(), font));
			table.addCell(buildCell(wayBill.getcSendAddress(), font));
			table.addCell(buildCell(wayBill.getcRecName(), font));
			table.addCell(buildCell(wayBill.getcRecMobile(), font));
			table.addCell(buildCell(wayBill.getcRecAddress(), font));
		}
		// 将表格加入文档
		document.add(table);

		document.close();

	}

	private Cell buildCell(String content, Font font)
			throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		return new Cell(phrase);
	}

	@RequestMapping(value = "/exportJasperPdf", method = { RequestMethod.POST })
	public void exportJasperPdf(WayBillSearchRequest ref, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, JRException, SQLException {
		String orderNum;
		String sendAddress;
		String recAddress;
		String sendProNum;
		Integer signStatus;
		if(StringUtils.isEmpty(ref.getOrderNum())){
			orderNum = null;
		}else{
			orderNum = ref.getOrderNum();
		}
		if(StringUtils.isEmpty(ref.getSendAddress())){
			sendAddress = null;
		}else{
			sendAddress = ref.getSendAddress();
		}
		if(StringUtils.isEmpty(ref.getRecAddress())){
			recAddress = null;
		}else{
			recAddress = ref.getRecAddress();
		}
		if(StringUtils.isEmpty(ref.getSendProNum())){
			sendProNum = null;
		}else{
			sendProNum = ref.getSendProNum();
		}
		if(StringUtils.isEmpty(ref.getSignStatus())){
			signStatus = 0;
		}else{
			signStatus = Integer.valueOf(ref.getSignStatus());
		}
		// 查询出 满足当前条件 结果数据
		List<WayBillDTO> wayBills = wayBillService.findWayBills(orderNum, sendAddress, recAddress, sendProNum, signStatus);

		// 下载导出
		// 设置头信息
		response.setContentType("application/pdf");
		String filename = "运单数据.pdf";
		String agent = request.getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);

		// 根据 jasperReport模板 生成pdf
		// 读取模板文件
		String jrxml = WayBillController.class.getClassLoader().getResource("jasper/waybill.jrxml").getPath();
		JasperReport report = JasperCompileManager.compileReport(jrxml);

		// 设置模板数据
		// Parameter变量
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("company", "駃达快递");
		// Field变量
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters,
				new JRBeanCollectionDataSource(wayBills));
		// 生成PDF客户端
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
		exporter.exportReport();// 导出
		response.getOutputStream().close();

	}
}

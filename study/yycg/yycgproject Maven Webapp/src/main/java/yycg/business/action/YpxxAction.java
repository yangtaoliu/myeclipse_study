package yycg.business.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.vo.PageQuery;
import yycg.base.process.context.Config;
import yycg.base.process.result.DataGridResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.SystemConfigService;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;
import yycg.business.service.YpxxService;
import yycg.util.ExcelExportSXXSSF;
import yycg.util.HxlsOptRowsInterface;
import yycg.util.HxlsRead;
import yycg.util.MyUtil;
import yycg.util.UUIDBuild;

@Controller
@RequestMapping("/ypml")
public class YpxxAction {
	@Autowired
	YpxxService ypxxService;
	
	@Autowired
	HxlsOptRowsInterface ypxxImportService;
	
	@Autowired
	SystemConfigService systemConfigService;
	// 导出页面展示
	@RequestMapping("/exportYpxx")
	public String exportYpxx(Model model) throws Exception {
		
		//药品类别
		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
		
		//药品交易状态
		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
		
		model.addAttribute("yplblist", yplblist);
		model.addAttribute("jyztlist", jyztlist);
		
		return "/business/ypml/exportYpxx";
	}
	
	//药品查询
	//用户查询页面的结果集
	//最终DataGridResultInfo通过@ResponseBody注解转成json格式
	//java对象的属性，转成json变成key,List集合转json后，变成数组
	@RequestMapping("/queryYpxxResult")
	public @ResponseBody DataGridResultInfo queryUserResult(YpxxQueryVo ypxxQueryVo, 
														int page, //DataGrid自动传递的页数
														int rows) //DataGrid自动传递的每页行数
														throws Exception{
		
		ypxxQueryVo = ypxxQueryVo!=null? ypxxQueryVo:new YpxxQueryVo();
		
		int total = ypxxService.findYpxxCount(ypxxQueryVo);
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);
		
		ypxxQueryVo.setPageQuery(pageQuery);
		
		List<YpxxCustom> list = ypxxService.findYpxxList(ypxxQueryVo);
		
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setRows(list);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;
	}
	
	
	
	// 导出提交
	@RequestMapping("/exportYpxxSubmit")
	public @ResponseBody SubmitResultInfo exportYpxxSubmit(YpxxQueryVo ypxxQueryVo) throws Exception {
		// 导出文件存放的路径，并且是虚拟目录指向的路径
		//String filePath = "d:/upload/linshi/";
		//改为从系统参数配置表获取参数值 
		String filePath = systemConfigService.findBasicinfoById("00301").getValue();		
		// 导出文件的前缀
		String filePrefix = "ypxx";
		// -1表示关闭自动刷新，手动控制写磁盘的时机，其它数据表示多少数据在内存保存，超过的则写入磁盘
		int flushRows = 100;		
		// 定义导出数据的title
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add("流水号");
		fieldNames.add("通用名");
		fieldNames.add("剂型");
		fieldNames.add("规格");
		fieldNames.add("转换系数");
		fieldNames.add("生产企业");
		fieldNames.add("商品名称");
		fieldNames.add("中标价格");
		fieldNames.add("交易状态");
		
		// 告诉导出类数据list中对象的属性，让ExcelExportSXXSSF通过反射获取对象的值
		List<String> fieldCodes = new ArrayList<String>();
		fieldCodes.add("bm");// 药品流水号
		fieldCodes.add("mc");// 通用名
		fieldCodes.add("jx");
		fieldCodes.add("gg");
		fieldCodes.add("zhxs");
		fieldCodes.add("scqymc");
		fieldCodes.add("spmc");
		fieldCodes.add("zbjg");
		fieldCodes.add("jyztmc");	
		
		// 上边的代码可以优化为，将title和title对应的 pojo的属性，使用map存储
		// ....
		// 注意：fieldCodes和fieldNames个数必须相同且属性和title顺序一一对应，这样title和内容才一一对应
		// 开始导出，执行一些workbook及sheet等对象的初始创建
		
		ExcelExportSXXSSF excelExportSXXSSF = ExcelExportSXXSSF.start(filePath,
				"/upload/", filePrefix, fieldNames, fieldCodes, flushRows);	
		
		// 导出的数据通过service取出
		List<YpxxCustom> list = ypxxService.findYpxxList(ypxxQueryVo);
		
		// 执行导出
		excelExportSXXSSF.writeDatasByObject(list);
		// 输出文件，返回下载文件的http地址，已经包括虚拟目录		
		String webpath = excelExportSXXSSF.exportFile();
		
		System.out.println(webpath);
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(
				Config.MESSAGE, 313, new Object[] {
						list.size(),
						webpath//下载地址
				}));
	}
	
	//药品导入页面
	@RequestMapping("/importypxx")
	public String importypxx(Model model)throws Exception{
		
		return "/business/ypml/importypxx";
	}	
	
	
	//药品导入提交
	@RequestMapping("/importypxxsubmit")
	public @ResponseBody SubmitResultInfo importypxxsubmit(
			//写上传的文件
			MultipartFile ypxximportfile,HttpServletRequest request
			)throws Exception{
		
		//将上传的文件写到磁盘
		String originalFilename  = ypxximportfile.getOriginalFilename();
		//写入磁盘的文件
		File file = new File("D:/upload/linshi/"+UUIDBuild.getUUID()+originalFilename.substring(originalFilename.lastIndexOf(".")));
		if(!file.exists()){
			//如果文件目录 不存在则创建
			file.mkdirs();
		}
		
		//将内存中的文件写磁盘
		ypxximportfile.transferTo(file);
		//上传文件磁盘上路径 
		String absolutePath = file.getAbsolutePath();
		
		
		//调用工具类进行药品目录 导入
		HxlsRead xls2csv = null;
		try {
			//第一个参数就是导入的文件
			//第二个参数就是导入文件中哪个sheet
			//第三个参数导入接口的实现类对象
			xls2csv = new HxlsRead(absolutePath,1,ypxxImportService);
			xls2csv.process();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//提示导入成功条数
		long success_num = xls2csv.getOptRows_success();
		//导入失败数量
		long failure_num = xls2csv.getOptRows_failure();
		

		
		if(failure_num>0){
			
			//对导入失败记录处理
			//获取导入失败记录
			List<List<String>> failRow = xls2csv.getFailrows();
			//获取导入记录的title
			List<String> rowTitle = xls2csv.getRowtitle();
			//获取导入失败原因
			List<String> failMsgs = xls2csv.getFailmsgs();
			
			//将上边获取到的失败记录、title、失败原因，导出成一个 excel
			//使用工具类进行导出，得到导出文件下载路径
			//......		
			
			//String webpath = this.exportFailRows(failRow,rowTitle,failMsgs);
			
			String webpath = this.exportFailRows(request,failRow,failMsgs);
			
			return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 315, new Object[]{
					success_num,failure_num,webpath
			}));			
		}
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 314, new Object[]{
				success_num,failure_num
		}));
	}	
	
	
	//模板开发HSSF
	public String exportFailRows(HttpServletRequest request, List<List<String>> failRow, List<String> failMsgs) throws Exception{   //inputDate yyyy-MM 只是年月格式，不是一个日期
		
		//InputStream in = new FileInputStream(new File("d:/tOUTPRODUCT.xls"));  
		//linux下jdk1.8 方法获取时，不会拼接自己写的目录 获取/真实路径再在后面加上即可
		String path = request.getSession().getServletContext().getRealPath("/") + "template/";
		InputStream in = new FileInputStream(new File(path + "ypxx_fail_template.xls")); 
		
		Workbook wb = new HSSFWorkbook(in);	//打开一个工作簿
		Sheet sheet = wb.getSheetAt(1);		//打开第二个sheet工作表，从0开始
		Row nRow = null;
		Cell nCell = null;
		int colNum = 0;		//列号
		int rowNum = 0;		//行号	
		
		//获取模板上数据的样式,第二行
		nRow = sheet.getRow(1);
		
		//通用名称样式
		nCell = nRow.getCell(0);
		CellStyle  mcStyle =  nCell.getCellStyle();
		
		//剂型样式
		nCell = nRow.getCell(1);
		CellStyle  jxStyle =  nCell.getCellStyle();
		
		//规格 样式
		nCell = nRow.getCell(2);
		CellStyle  ggStyle =  nCell.getCellStyle();
		
		//转换系数样式
		nCell = nRow.getCell(3);
		CellStyle  zhxsStyle =  nCell.getCellStyle();
		
		//中标价格样式
		nCell = nRow.getCell(4);
		CellStyle  zbjgStyle =  nCell.getCellStyle();
				
		//生产企业样式
		nCell = nRow.getCell(5);
		CellStyle  scqymcStyle =  nCell.getCellStyle();
					
		//商品名样式
		nCell = nRow.getCell(6);
		CellStyle  spmcStyle =  nCell.getCellStyle();
							
		//交易状态样式
		nCell = nRow.getCell(7);
		CellStyle  jyztStyle =  nCell.getCellStyle();
					
		
		//错误信息样式
		nCell = nRow.getCell(8);
		CellStyle  cwxxStyle =  nCell.getCellStyle();		
		
		//静态文字不用设置了，直接行号加一
		rowNum++;		
		
		
/*		String[] title = new String[]{"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
		for(int i=0; i<title.length; i++){
			nCell = nRow.createCell(i+1);
			nCell.setCellValue(title[i]);
			nCell.setCellStyle(this.title(nStyle, nFont));
		}*/	
		
		//换行处理数据
		for(int j=0;j<failRow.size();j++){
			
			colNum = 0;		//列号
			
			List<String> rowData = failRow.get(j);
			
			nRow = sheet.createRow(rowNum++);
			nRow.setHeightInPoints(24);
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(0));	
			nCell.setCellStyle(mcStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(1));	
			nCell.setCellStyle(jxStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(2));	
			nCell.setCellStyle(ggStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(3));	
			nCell.setCellStyle(zhxsStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(4));	
			nCell.setCellStyle(zbjgStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(5));	
			nCell.setCellStyle(scqymcStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(6));	
			nCell.setCellStyle(spmcStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(rowData.get(7));	
			nCell.setCellStyle(jyztStyle);
			
			nCell = nRow.createCell(colNum++);
			nCell.setCellValue(failMsgs.get(j));	
			nCell.setCellStyle(cwxxStyle);		
		}

		
		/*OutputStream os = new FileOutputStream(new File("d:/a.xls"));
		wb.write(os);
		os.flush();
		os.close();
		*/
		String filePath = systemConfigService.findBasicinfoById("00301").getValue();
		
		String filename = "ypxx_template"+"_"+MyUtil.getCurrentTimeStr() + ".xls";
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + filename);
		wb.write(out);
		out.flush();
		out.close();
		
		return "/upload/" + filename;	
		
	}	
	
}

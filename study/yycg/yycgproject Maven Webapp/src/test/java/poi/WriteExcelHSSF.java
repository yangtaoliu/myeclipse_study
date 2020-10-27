package poi;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * poi测试导出excel文件，数据量大出现内存溢出
 * @author Thinkpad
 *
 */
public class WriteExcelHSSF {
   
	public static void main(String[] args) throws IOException {

		// 创建文件输出流
		FileOutputStream out = new FileOutputStream("d:/workbook.xls");
		// 创建一个工作簿
		Workbook wb = new HSSFWorkbook();
			
		for (int j = 0; j < 1; j++) {
			Sheet s = wb.createSheet();//创建1个sheet
			wb.setSheetName(j, "sheet" + j);//指定sheet的名称
			//xls文件最大支持65536行
			for (int rownum = 0; rownum < 15; rownum++) {//创建行,.xls一个sheet中的行数最大65535
				// 创建一行
				Row r = s.createRow(rownum);

				for (int cellnum = 0; cellnum < 10; cellnum ++) {//一行创建10个单元格
					// 在行里边创建单元格
					Cell c = r.createCell(cellnum);
					//向单元格写入数据
					c.setCellValue(cellnum);

				}

			}

		}
		System.out.println("int..............");
		wb.write(out);//输出文件内容
		
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
	}

}

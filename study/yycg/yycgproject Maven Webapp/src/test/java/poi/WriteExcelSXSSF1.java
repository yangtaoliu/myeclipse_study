package poi;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 采用SXSSF导出excel不出现内存溢出
 * @author mrt
 *
 */
public class WriteExcelSXSSF1 {

	public static void main(String[] args) throws Throwable {
		
		//创建一个SXSSFWorkbook
		SXSSFWorkbook wb = new SXSSFWorkbook(-1); // turn off auto-flushing and accumulate all rows in memory
		//创建一个sheet
		Sheet sh = wb.createSheet();
    for(int rownum = 0; rownum < 100000; rownum++){
    	//创建一个行
        Row row = sh.createRow(rownum);
        for(int cellnum = 0; cellnum < 10; cellnum++){//创建单元格
            Cell cell = row.createCell(cellnum);
            String address = new CellReference(cell).formatAsString();//单元格地址
            cell.setCellValue(address);
        }

       // manually control how rows are flushed to disk 
       if(rownum % 10000 == 0) {//一万行向磁盘写一次
    	  
            ((SXSSFSheet)sh).flushRows(100); // retain 100 last rows and flush all others
            //Thread.sleep(1000);
            System.out.println("写入....");
            // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
            // this method flushes all rows
       }

    }
    FileOutputStream out = new FileOutputStream("d:/test.xlsx");
    wb.write(out);//将临时文件合并，写入最终文件
    
    out.close();

    // dispose of temporary files backing this workbook on disk
    wb.dispose();

	}

}

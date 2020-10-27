package cn.study.elec.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	/**
	 * 将日期类型转换成String类型，file的格式
	 * @param date
	 * @return
	 */
	public static String dateToStringByFile(Date date) {
		String sDate = new SimpleDateFormat("/yyyy/MM/dd/").format(date);
		return sDate;
	}

}

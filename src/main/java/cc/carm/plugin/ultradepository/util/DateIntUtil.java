package cc.carm.plugin.ultradepository.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateIntUtil {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");


	public static SimpleDateFormat getFormat() {
		return FORMAT;
	}

	public static int getCurrentDate() {
		return getDateInt(new Date(System.currentTimeMillis()));
	}

	public static int getDateInt(Date date) {
		return Integer.parseInt(getFormat().format(date));
	}

	public static long getDateMillis(int dateInt) {
		return getDate(dateInt).getTime();
	}

	public static Date getDate(int dateInt) {
		try {
			long millis = getFormat().parse(Integer.toString(dateInt)).getTime();
			return new java.sql.Date(millis);
		} catch (ParseException | NumberFormatException e) {
			return new Date(System.currentTimeMillis());
		}
	}


}

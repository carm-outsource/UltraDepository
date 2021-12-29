package cc.carm.plugin.ultradepository.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

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
		try {
			return getFormat().parse(Integer.toString(dateInt)).getTime();
		} catch (ParseException e) {
			return System.currentTimeMillis();
		}
	}


}

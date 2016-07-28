package com.caomei.comingvagetable.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtil {
	public static String getDateString(int index) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp now = new Timestamp(System.currentTimeMillis()); 
		String sf = sdf.format(now);
		return sf;
	}
}

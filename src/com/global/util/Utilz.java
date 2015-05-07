package com.global.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utilz {
	/**
	 * 获取指定样式的当前时间
	 * @param timeStyle
	 * @return
	 */
	public static String getCurrentFomatTime(String timeStyle){
		Calendar calendar = Calendar.getInstance();
    	long time = calendar.getTimeInMillis();
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeStyle);
    	return simpleDateFormat.format(time);
	}
	
	
}

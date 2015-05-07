package com.global.format;

import java.text.SimpleDateFormat;

public class Format {
	private static SimpleDateFormat dateformat;

	public static SimpleDateFormat getDateFormat() {
		if (dateformat == null) {
			dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
		return dateformat;
	}

}

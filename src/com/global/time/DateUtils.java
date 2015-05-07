package com.global.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	public static final String HTTP_RESPONSE_DATE_HEADER = "EEE, dd MMM yyyy HH:mm:ss zzz";

	public static final SimpleDateFormat responseHeaderFormat = new SimpleDateFormat(HTTP_RESPONSE_DATE_HEADER, Locale.US);

	static {
		responseHeaderFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static Date firstDayInNextMonth(Date day) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static int getDateInWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static Date getDateNextDay(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, n);
		return c.getTime();
	}

	public static Date getDateNextMonth(int n) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.MONTH, now.get(Calendar.MONTH) + n);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}

	public static int getDateOfMoth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static List<Date> getDatesInMonth() {

		List<Date> dates = new ArrayList<Date>();

		Calendar c = Calendar.getInstance();

		c.set(Calendar.DATE, 1);

		int maxDay = c.getActualMaximum(Calendar.DATE);

		for (int i = 1; i <= maxDay; i++) {
			c.set(Calendar.DATE, i);
			dates.add(c.getTime());
		}

		return dates;
	}

	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static Date getMonthEnd() {
		int length = getDateOfMoth();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, length);
		c.set(Calendar.HOUR, 24);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getMonthEnd(Date date) {
		if (date == null) {
			return null;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		start.set(Calendar.MONTH, start.get(Calendar.MONTH) + 1);
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.HOUR, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		return start.getTime();
	}

	public static Date getMonthStart(Date date) {
		if (date == null) {
			return null;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.HOUR, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		return start.getTime();
	}

	public static Date getMorning() {
		return getMorning(new Date());
	}

	public static Date getMorning(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getMorningNextDate(int n) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE) + n);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}

	public static Date getNextMonth(int nextStep) {
		Calendar c = Calendar.getInstance();
		int m = c.get(Calendar.MONTH);
		c.set(Calendar.MONTH, m + nextStep);
		return c.getTime();
	}

	public static Date getNextMonthToday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
		return c.getTime();
	}

	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static Date getYearEnd(int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static Date getYearStart(int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
}

package com.global.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
	public static final long ONE_MINUTS = 60 * 1000;
	public static final long ONE_HOUR = ONE_MINUTS * 60;;
	public static final long ONE_DAY = ONE_HOUR * 24;

	public static final long getTimeZoneOffset() {
		return TimeZone.getDefault().getRawOffset()
				- TimeZone.getTimeZone("GMT+8:00").getRawOffset();
	}

	public static String formatTime(Date uptime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(uptime);
	}

	public static String getHHmm(long time) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(time);
	}

	public static String getMMdd(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		return format.format(time);
	}

	public static String getOrderTime(long stime) {
		stime = stime - getTimeZoneOffset();
		String t;
		if (isBeforeToday(stime)) {
			t = getMMdd(stime);
		} else if (stime - System.currentTimeMillis() < ONE_MINUTS * 20
				&& stime - System.currentTimeMillis() >= 0) {
			long tt = (stime - System.currentTimeMillis()) / ONE_MINUTS;
			tt = tt < 1 ? 1 : tt;
			t = tt + "分钟内";
		} else if (isToday(stime)) {
			t = "今天 " + getHHmm(stime);
		} else if (isTomorrow(stime)) {
			t = "明天 " + getHHmm(stime);
		} else {
			t = getMMdd(stime) + getHHmm(stime);
		}
		return t;
	}

	public static String getHistoryTime(long stime) {
		stime = stime - getTimeZoneOffset();
		String t;
		if (isBeforeToday(stime)) {
			t = getMMdd(stime);
		} else if (isToday(stime)) {
			t = "今天 " + getHHmm(stime);
		} else if (isTomorrow(stime)) {
			t = "明天 " + getHHmm(stime);
		} else {
			t = getMMdd(stime);
		}
		return t;
	}

	public static String getTimeNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}

//	public static String timeBefore(long time) {
//		long now = System.currentTimeMillis();
//		int diff = (int) Math.ceil((now - time) / ONE_MINUTS);
//		if (diff <= 0) {
//			diff = 1;
//		}
//		return diff + " 分钟前";
//	}

	public static int timeBefore(long time) {
		long now = System.currentTimeMillis();
		int diff = (int) Math.ceil((now - time) / ONE_MINUTS);
		if (diff <= 0) {
			diff = 1;
		}
		return diff;
	}

	public static String getChatTime(long time) {
		SimpleDateFormat format;
		long start = DateUtils.getMorning().getTime();
		if (time >= start) {
			if (time - start < ONE_DAY)
				format = new SimpleDateFormat("今天 HH:mm");
			else
				format = new SimpleDateFormat("MM月dd日 HH:mm");
		} else {
			format = new SimpleDateFormat("MM月dd日 HH:mm");
		}
		return format.format(new Date(time));
	}

	public static String getRecordTime(long time) {
		long start = DateUtils.getMorning().getTime();
		if (time >= start) {
			return getHHmm(time);
		} else {
			return getMMdd(time);
		}
	}

	public static String getPublishTime(long relTime) {
		long time = relTime + Calendar.getInstance().getTimeInMillis();
		String t;
		if (relTime < ONE_MINUTS * 5) {
			t = "马上用车";
		} else if (relTime <= ONE_HOUR) {
			t = relTime / ONE_MINUTS + "分钟内";
		} else if (isToday(time)) {
			t = "今天 " + getHHmm(time);
		} else if (isTomorrow(time)) {
			t = "明天 " + getHHmm(time);
		} else {
			t = getMMdd(time);
		}
		return t;
	}

	public static boolean isToday(long time) {
		long start = DateUtils.getMorning().getTime();
		long end = DateUtils.getMorningNextDate(1).getTime();
		if (time > start && time < end) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isTomorrow(long time) {
		long start = DateUtils.getMorningNextDate(1).getTime();
		long end = DateUtils.getMorningNextDate(2).getTime();
		if (time > start && time < end) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBeforeToday(long time) {
		long start = DateUtils.getMorning().getTime();
		if (time < start) {
			return true;
		} else {
			return false;
		}
	}
}

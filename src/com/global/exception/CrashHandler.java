package com.global.exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.global.app.SysApplicationImpl;
import com.global.log.CLog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler INSTANCE = new CrashHandler();
	private Context mContext;
	private Map<String, String> info = new HashMap<String, String>();
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	private String exceptionMessage;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		SysApplicationImpl.exit();

	}

	public boolean handleException(Throwable ex) {
		if (ex == null)
			return false;
		collectDeviceInfo(mContext);
		saveCrashInfo2File(ex);

		SysApplicationImpl.setSharedPref(mContext, SysApplicationImpl.ISLOGIN,
				"empty");
		SysApplicationImpl.restart();

		return true;
	}

	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
				CLog.i(field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private String saveCrashInfo2File(Throwable ex) {

		// temp
		ex.printStackTrace();

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\r\n");
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();
		String result = writer.toString();
		sb.append(result);

		long timetamp = System.currentTimeMillis();
		String time = format.format(new Date());
		String fileName = "crash-" + time + "-" + timetamp + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + "crash");
				CLog.i(dir.toString());
				if (!dir.exists())
					dir.mkdir();
				FileOutputStream fos = new FileOutputStream(new File(dir,
						fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
				return sb.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

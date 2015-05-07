/**
 * author daishulin@163.com
 * 2013-7-13
 */
package com.global.app;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.global.alert.AlertWaitService;
import com.global.db.DatabaseManager;
import com.global.db.DbHelper;
import com.global.db.DBPreferences;
import com.global.log.CLog;
import com.global.time.TimeUtils;
import com.global.toast.Toaster;

public class SysApplicationImpl extends Application {
	private static Map<String, WeakReference<Activity>> mList = new HashMap<String, WeakReference<Activity>>();
	private static Stack<WeakReference<Activity>> currentActivity = null;
	private static SysApplicationImpl instance;
	public Object object;

	private static int loginTimeout = 24 * 60;
	private static String isdebug = Messages
			.getString("SysApplicationImpl.isdebug");
	private static String isTest = Messages
			.getString("SysApplicationImpl.isTest");
	private static String mLastLoginTime = "empty";

	public static boolean isLogin() {
		if (!mLastLoginTime.equals("empty")) {
			long time = Long.parseLong(mLastLoginTime);
			CLog.i("lastlogin=" + String.valueOf(time));
			CLog.i("lastlogin=" + TimeUtils.timeBefore(time));
			if (TimeUtils.timeBefore(time) < loginTimeout) {
				return true;
			}
			;
		}
		return false;
	}

	public static void restart() {
		ActivityManager activityManager = (ActivityManager) getInstance()
				.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
		activityManager.restartPackage(getInstance().getPackageName());
	}

	public static void jumpToLogin(Context context) {
		Toaster.getInstance().displayToast("请重新登录");
		cleanLogin();
		Intent intent;
		try {
			intent = new Intent(context,
					Class.forName("com.hjdata.ui.login.LoginActivity"));
			context.startActivity(intent);
		} catch (ClassNotFoundException e) {
		}
	}

	public synchronized static SysApplicationImpl getInstance() {
		return instance;
	}

	// add Activity
	public static void addActivity(Activity activity) {
		mList.put(activity.getClass().getName(), new WeakReference<Activity>(
				activity));
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		isFirstCreate();
		currentActivity = new Stack<WeakReference<Activity>>();
		// if (isDebug()) {
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(this);
		// }
		Intent intent = new Intent(this, AlertWaitService.class);
		startService(intent);

		PackageManager pm = getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(getPackageName(), 0);
		} catch (Exception e) {
		}
		DatabaseManager.initializeInstance(DbHelper.init(
				getApplicationContext(), "creawayDb", pi.versionCode));
		Toaster.init(getApplicationContext());
		mLastLoginTime = getLastLoginTime();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public boolean isDebug() {
		if (isdebug.equals("true")) {
			return true;
		}

		return false;
	}

	public boolean isTest() {
		if (isTest.equals("true")) {
			return true;
		}

		return false;
	}

	public static void cleanLogin() {

		cleanActivity();
	}

	public static void isFirstCreate() {

	}

	public static void cleanActivity() {
		try {
			for (WeakReference<Activity> activity : mList.values()) {
				if (activity != null
						&& activity.get() != null
						&& !activity.get().getClass().getName()
								.equals("com.hjdata.ui.login.LoginActivity")) {
					CLog.i(activity.get().getClass().getName());
					activity.get().finish();
				}
			}
		} catch (Exception e) {
			if (SysApplicationImpl.getInstance().isDebug()) {
				e.printStackTrace();
			}

		}
	}

	public static void cleanAllActivity() {
		try {
			for (WeakReference<Activity> activity : mList.values()) {
				if (activity != null && activity.get() != null) {
					CLog.i(activity.get().getClass().getName());
					activity.get().finish();
				}
			}
		} catch (Exception e) {
			CLog.e(e);
			if (SysApplicationImpl.getInstance().isDebug()) {
				e.printStackTrace();
			}

		}
	}

	public static void exit() {
		System.exit(1);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	public boolean setTopActivity(Activity acitvity) {
		if (currentActivity != null && !currentActivity.isEmpty()) {
			currentActivity.pop();
		}
		if (currentActivity != null)
			currentActivity.push(new WeakReference<Activity>(acitvity));
		return true;
	}

	public static String getSharedPref(Context context, String key) {
		return DBPreferences.getInstance().getString(key, "empty");
	}

	public static String getSharedPref(String key) {
		return DBPreferences.getInstance().getString(key, "empty");
	}

	public static void setSharedPref(Context context, String key, String value) {
		DBPreferences.getInstance().edit().putString(key, value).commit();
	}

	public static void setSharedPref(String key, String value) {
		DBPreferences.getInstance().edit().putString(key, value).commit();
	}

	public Activity getTopActivity() {
		return currentActivity.get(0).get();
	}

	public static String UNSERVERY_DATA_KEY = "UNSERVERY_DATA";
	public static String SERVERYING_DATA_KEY = "SERVERYING_DATA";
	public static String HOUSEDETAILS = "HOUSEDETAILS";
	public static String SERVERINGDETAILS = "SERVERINGDETAILS";
	public static String ISLOGIN = "ISLOGIN";
	public static String USERNAME = "USERNAME";
	public static String ISFIRSTLOGIN = "ISFIRSTLOGIN";

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getLastOperatorId() {

		return "";
	}

	public String getLastLoginTime() {

		return "";
	}

	public void setLastOperatorId(String operatorid) {

	}

	public static enum SBLX {
		SBLX01 {
			@Override
			public String getChString() {
				return "终端故障";
			}
		},
		SBLX02 {
			@Override
			public String getChString() {
				return "电表故障";
			}
		};

		public abstract String getChString();

		public static SBLX getInstance(String lx) {
			if (lx.equals("01")) {
				return SBLX01;
			} else if (lx.equals("02")) {
				return SBLX02;
			} else {
				return SBLX01;

			}
		}
	};
}
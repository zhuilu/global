package com.global.log;

import android.database.Cursor;
import android.util.Log;

import com.global.app.SysApplicationImpl;

public class CLog {
	private final static String TAG = "CLog";//

	public static void i(String mes) {
		if (SysApplicationImpl.getInstance().isDebug()) {
			if (mes == null) {
				mes = "null";
			}
			Log.i(TAG, mes);
		}
	}

	public static void e(String mes) {
		if (SysApplicationImpl.getInstance().isDebug()) {
			if (mes == null) {
				mes = "null";
			}
			CLog.e(mes);
		}
	}

	public static void i(Cursor cursor) {
		if (SysApplicationImpl.getInstance().isDebug()) {
			if (cursor != null) {
				Log.i(TAG, "cursor.getCount()= " + cursor.getCount());
				while (cursor != null && cursor.moveToNext()) {
					StringBuffer buffer = new StringBuffer("");
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						String columnName = cursor.getColumnName(i);
						String columnValue = cursor.getString(cursor
								.getColumnIndex(columnName));

						buffer.append(columnName + "= " + columnValue
								+ "  ;\t\n");

					}
					Log.i(TAG, buffer.toString());
					buffer = null;
				}
				cursor.moveToFirst();
				cursor.moveToLast();
			} else {
				i("无效 cursor");
			}

		}
	}

	public static void e(Throwable mes) {
		if (SysApplicationImpl.getInstance().isDebug()) {
			for (StackTraceElement ele : mes.getStackTrace()) {
				if (ele == null) {
					i("无效 StackTraceElement");
					return;
				}
				CLog.e(ele.toString());

			}
		}
	}
}

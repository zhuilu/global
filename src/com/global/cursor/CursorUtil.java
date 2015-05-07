package com.global.cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.database.Cursor;
import com.global.log.CLog;

public class CursorUtil {

	public static Map<String, String> cursorToMap(Cursor cursor, String key,
			String value) {
		Map<String, String> map = new HashMap<String, String>();
		if (cursor != null) {
			CLog.i("CursorUtil cursor.getCount()==" + cursor.getCount());
			while (cursor != null && cursor.moveToNext()) {
				String columnName = cursor
						.getString(cursor.getColumnIndex(key));
				String columnValue = cursor.getString(cursor
						.getColumnIndex(value));
				map.put(columnName, columnValue);
			}
			cursor.close();

		}
		return map;
	}

	public static List<Map<String, String>> cursorToMap(Cursor cursor) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		while (cursor != null && cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			for (int p = 0; p < cursor.getColumnCount(); p++) {
				String columnName = cursor.getColumnName(p);
				String columnValue = cursor.getString(cursor
						.getColumnIndex(columnName));
				map.put(columnName, columnValue);
			}
			dataList.add(map);

		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return dataList;
	}
}
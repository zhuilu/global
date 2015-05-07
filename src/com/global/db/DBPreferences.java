package com.global.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.global.app.SysApplicationImpl;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBPreferences implements SharedPreferences {
	public static DBPreferences dbPreferences;
	HashSet<SharedPreferences.OnSharedPreferenceChangeListener> mListeners;
	private static final String defaultName = "dbPreferences_table";
	private static String mName = "";

	public static DBPreferences getInstance() {
		mName = defaultName;
		if (dbPreferences == null) {
			dbPreferences = new DBPreferences(null, SysApplicationImpl
					.getInstance().getApplicationContext());
		}
		createPrefs(mName);
		return dbPreferences;
	}

	public static DBPreferences getInstance(String name) {
		mName = name;
		if (dbPreferences == null) {
			dbPreferences = new DBPreferences(null, SysApplicationImpl
					.getInstance().getApplicationContext());
		}
		createPrefs(mName);
		return dbPreferences;
	}

	public DBPreferences(SQLiteDatabase sqliteDatabase, Context context) {
		createPrefs(mName);
	}

	private static void createPrefs(String name) {
		String sql = "CREATE TABLE IF NOT EXISTS " + name + " (" + "_id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + "pref_name"
				+ " TEXT NOT NULL UNIQUE, " + "pref_value" + " TEXT" + ");";
		DbHelper.getInStance().getWritableDatabase().execSQL(sql);

	}

	public void deletePrefs(String name) {
		String sql = "DROP TABLE IF EXISTS " + name + ";";
		DbHelper.getInStance().getWritableDatabase().execSQL(sql);
	}

	@Override
	public Map<String, ?> getAll() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Cursor cursor = null;
		try {
			cursor = DbHelper.getInStance().getWritableDatabase()
					.query(mName, null, null, null, null, null, null);
			while (cursor.moveToNext()) {
				hashMap.put(cursor.getString(1), cursor.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return hashMap;
	}

	@Override
	public String getString(String key, String defValue) {
		String value = defValue;
		Cursor cursor = DbHelper
				.getInStance()
				.getWritableDatabase()
				.query(mName, null, "pref_name = ?", new String[] { key },
						null, null, null);
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			value = cursor.getString(2);
		}
		if (cursor != null)
			cursor.close();
		return value;
	}

	@Override
	public int getInt(String key, int defValue) {
		String str = getString(key, null);
		if (str == null)
			return defValue;
		else
			return Integer.parseInt(str);
	}

	@Override
	public long getLong(String key, long defValue) {
		String str = getString(key, null);
		if (str == null)
			return defValue;
		else
			return Long.parseLong(str);
	}

	@Override
	public float getFloat(String key, float defValue) {
		String str = getString(key, null);
		if (str == null)
			return defValue;
		else
			return Float.parseFloat(str);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		String str = getString(key, null);
		if (str == null)
			return defValue;
		else
			return Boolean.parseBoolean(str);
	}

	@Override
	public boolean contains(String key) {
		boolean bool = false;
		Cursor cursor = DbHelper
				.getInStance()
				.getWritableDatabase()
				.query(mName, null, "pref_name = ?", new String[] { key },
						null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			bool = true;
		} else {
			bool = false;
		}
		if (cursor != null)
			cursor.close();
		return bool;
	}

	@Override
	public Editor edit() {
		return new DBEditor(this, mName);
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		mListeners.add(listener);

	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		mListeners.remove(listener);

	}

	// @Override
	public Set<String> getStringSet(String arg0, Set<String> arg1) {
		String stringset = getString(arg0, null);
		if (stringset == null) {
			return arg1;
		}

		HashSet<String> set = new HashSet<String>();
		String[] vals = stringset.split("~");
		for (int i = 0; i < vals.length; i++) {
			String val = vals[i];
			set.add(val.replaceAll("^t", "~").replaceAll("^^", "^"));
		}
		return set;
	}

}

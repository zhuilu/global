package com.global.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;

/*
 * lining
 * 
 * @ 通过DBEditor的方式提交设置信息到db
 */

public class DBEditor implements Editor {

	private DBPreferences mPrefs;
	private Set<String> mRemove;
	private HashMap<String, String> mVals;
	private String mName;

	public DBEditor(DBPreferences dbPreferences, String name) {
		this.mPrefs = dbPreferences;
		this.mVals = new HashMap<String, String>();
		this.mRemove = new HashSet<String>();
		this.mName = name;
	}

	public void apply() {
		commit();
	}

	@Override
	public Editor putString(String key, String value) {
		mVals.put(key, value);
		return this;
	}

	@Override
	public Editor putInt(String key, int value) {
		mVals.put(key, String.valueOf(value));
		return this;
	}

	@Override
	public Editor putLong(String key, long value) {
		mVals.put(key, String.valueOf(value));
		return this;
	}

	@Override
	public Editor putFloat(String key, float value) {
		mVals.put(key, String.valueOf(value));
		return this;
	}

	@Override
	public Editor putBoolean(String key, boolean value) {
		mVals.put(key, String.valueOf(value));
		return this;
	}

	/*
	 * lining
	 * 
	 * @see
	 * android.content.SharedPreferences.Editor#putStringSet(java.lang.String,
	 * java.util.Set)
	 */
	// @Override
	public Editor putStringSet(String key, Set<String> value) {
		StringBuilder stringset = new StringBuilder();
		boolean isFirst = true;
		Iterator<String> it = value.iterator();
		while (it.hasNext()) {
			String val = it.next();
			String escaped = val.replaceAll("^", "^^").replaceAll("~", "^t");
			if (!isFirst) {
				stringset.append(126);
			} else {
				isFirst = false;
			}
			stringset.append(escaped);

		}
		mVals.put(key, stringset.toString());
		return this;
	}

	@Override
	public Editor remove(String key) {
		mRemove.add(key);
		return this;
	}

	@Override
	public Editor clear() {
		mRemove = mPrefs.getAll().keySet();
		return this;
	}

	@Override
	public boolean commit() {
		SQLiteDatabase db = DbHelper.getInStance().getWritableDatabase();

		HashSet<String> changed = new HashSet<String>();
		db.beginTransaction();
		try {
			if (mRemove != null) {
				Iterator<String> strIt = mRemove.iterator();
				while (strIt.hasNext()) {
					String name = strIt.next();
					changed.add(name);
					db.delete(mName, "pref_name = ?", new String[] { name });
				}
			}

			if (mVals != null) {
				Iterator<Entry<String, String>> entryIt = mVals.entrySet()
						.iterator();
				while (entryIt.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) entryIt
							.next();
					changed.add(entry.getKey());
					ContentValues contentValues = new ContentValues();
					contentValues.put("pref_name", entry.getKey());
					contentValues.put("pref_value", entry.getValue());
					if (db.update(mName, contentValues, "pref_name= ?",
							new String[] { String.valueOf(entry.getKey()) }) == 0) {
						db.insert(mName, "pref_name", contentValues);
					}
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
		}

		if (this.mPrefs.mListeners != null) {
			Iterator<String> keyIt = changed.iterator();
			while (keyIt.hasNext()) {
				String changeKey = (String) keyIt.next();
				Iterator<SharedPreferences.OnSharedPreferenceChangeListener> listenerIt = mPrefs.mListeners
						.iterator();
				while (listenerIt.hasNext()) {
					SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = (SharedPreferences.OnSharedPreferenceChangeListener) listenerIt
							.next();
					onSharedPreferenceChangeListener.onSharedPreferenceChanged(
							mPrefs, changeKey);
				}
			}
		}
		return true;
	}
}

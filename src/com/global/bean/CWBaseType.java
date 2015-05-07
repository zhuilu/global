/**
 *   CWBaseType.java
 *	CreawayClient-Android
 *   类说明
 *
 *   Create by   on 2014-3-3 下午2:20:33
 *   Copyright (c) 2014 Creaway.com. All rights reserved.
 * 
 */
package com.global.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.global.db.DbHelper;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * @author daishulin@163.com
 */
public abstract class CWBaseType implements Serializable {

	protected String _id;

	public CWBaseType() {
	}

	public CWBaseType(JSONObject obj) {
	}

	public static String getNoneNullString(String str) {
		return str != null ? str : "";
	}

	public boolean commit() {
		ContentValues values = toContentValues();
		if (getTableName() == null) {
			throw new RuntimeException("getTableName() can not be null");
		}

		System.out.println(_id == null ? "_id == null" : "_id == " + _id);
		if (_id == null
				| DbHelper
						.getInStance()
						.getReadableDatabase()
						.update(getTableName(), values, "_id=?",
								new String[] { _id }) < 1) {
			_id = String.valueOf(DbHelper.getInStance().getReadableDatabase()
					.insert(getTableName(), "", values));
			System.out.println(getTableName() + " 插入一条记录 _id=" + _id);
		}

		return true;
	};

	public abstract String getTableName();

	public abstract ContentValues toContentValues();

	public abstract Object CursorToObj(Cursor cursor);

	public JSONObject toJSONData() {
		return null;
	}
}

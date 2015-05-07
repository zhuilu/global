package com.global.db;

import com.global.app.SysApplicationImpl;
import com.global.bean.Node;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private static DbHelper mHelper;

	public static DbHelper init(Context context, String name, int version) {
		if (mHelper == null) {
			mHelper = new DbHelper(context, name, version);
		}
		return mHelper;
	}

	public static synchronized DbHelper getInStance() {
		return mHelper;
	}

	public static String TABLE_NodeStep = "TABLE_NodeStep";

	private DbHelper(Context context, String name, int version) {
		super(context, name, null, version);
		if (SysApplicationImpl.getInstance().isDebug()
				&& this.getWritableDatabase() != null) {
			createTables(this.getWritableDatabase());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
		DBInitData.initNodeStep(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		drop(db);
		createTables(db);
		DBInitData.initNodeStep(db);
	}

	private void createTables(SQLiteDatabase db) {

		createNodeStep(db);// 创建调试步骤

	}

	// 调试步骤
	private void createNodeStep(SQLiteDatabase db) {
		try {
			db.execSQL(createTableString(TABLE_NodeStep, new String[] {
					Node.Columns.nodeId, Node.Columns.type,
					Node.Columns.fragmentName, Node.Columns.nodePId,
					Node.Columns.nodeName }));

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private String createTableString(String paramString, String[] columns) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
		stringBuilder.append(paramString);

		stringBuilder.append(" (" + "_id"
				+ " integer primary key autoincrement, ");
		for (int j = 0; j < columns.length; j++) {
			stringBuilder.append(columns[j]);
			if ((j + 1) < columns.length) {
				stringBuilder.append(", ");
			}
		}

		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	private void drop(SQLiteDatabase db) {
		db.execSQL("drop table if exists " + TABLE_NodeStep);
	}
}

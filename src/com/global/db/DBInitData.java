package com.global.db;

import com.global.bean.Node;

import android.database.sqlite.SQLiteDatabase;

public class DBInitData {

	public static void initNodeStep(SQLiteDatabase db) {
		db.beginTransaction();

		// 调试步骤：
		//

		db.insert(DbHelper.TABLE_NodeStep, null, new Node("100", null, "自定义控件",
				"0", "").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("101", "100",
				"WheelView", "0", "TextFragment").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("102", "100",
				"Pull refresh", "0", "PullRefreshFragment").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("103", "100",
				"读取分辨率", "0", "ReadScreenFragment").toContentValues());

		db.insert(DbHelper.TABLE_NodeStep, null, new Node("105", "100",
				"自动宽度TextView", "0", "AutoScaleTextViewFragment")
				.toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("106", "100",
				"StickListView", "0", "PinnedSectionListFragment")
				.toContentValues());

		db.insert(DbHelper.TABLE_NodeStep, null, new Node("200", null, "通信",
				"0", "").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("201", "200",
				"串口485通信", "0", "Serial485Fragment").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("202", "200", "载波调试",
				"0", "NativeMenuFragment").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("203", "200",
				"载波调试OLD", "0", "CopyOfSerial485Fragment").toContentValues());
		db.insert(DbHelper.TABLE_NodeStep, null, new Node("204", "200",
				"动画", "0", "AnimFragment").toContentValues());
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}

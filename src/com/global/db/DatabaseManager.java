package com.global.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager {
	private AtomicInteger mOpenCounter = new AtomicInteger();

	private static DatabaseManager instance;
	private static SQLiteOpenHelper MyDbHelper;
	private SQLiteDatabase mDatabase;

	public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
		if (instance == null) {
			instance = new DatabaseManager();
			MyDbHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					DatabaseManager.class.getSimpleName()
							+ " is not initialized, call initializeInstance(..) method first.");
		}

		return instance;
	}

	public synchronized SQLiteDatabase openDatabase() {
		System.out.println("开启数据库");
		if (mDatabase == null) {
			mDatabase = MyDbHelper.getWritableDatabase();
		}
		if (!mDatabase.isOpen()) {
			mDatabase = MyDbHelper.getWritableDatabase();
		}
		if (mDatabase.isReadOnly()) {
			mDatabase = MyDbHelper.getWritableDatabase();
		}
		return mDatabase;
	}

	public synchronized void closeDatabase() {
		System.out.println("关闭数据库");
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			System.out.println("关闭数据库成功");
		}

	}
}

//package com.global.db;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.nbcb.entity.ListItem;
//
///**
// * 证件类型
// * 
// * @author Administrator
// * 
// */
//public class ListItemDao {
//	private DBOpenHelper helper;
//	private ArrayList<ListItem> list;
//
//	public ListItemDao(Context context) {
//		helper = new DBOpenHelper(context);
//	}
//
//	public ArrayList<ListItem> getItems(String str) {
//		list = new ArrayList<ListItem>();
//		ListItem item;
//		SQLiteDatabase db = helper.getReadableDatabase(); //
//		Cursor c = db.query("tbl_list", new String[] { "_id", "listcode",
//				"title", "describe" }, "listcode=?", new String[] { str },
//				null, null, "title asc");
//		if (c != null && c.getCount() > 0) {
//			while (c.moveToNext()) {
//				item = new ListItem();
//				item.setId(c.getInt(c.getColumnIndex("_id")));
//				item.setListCode(c.getString(c.getColumnIndex("listcode")));
//				item.setTitle(c.getString(c.getColumnIndex("title")));
//				item.setDescribe(c.getString(c.getColumnIndex("describe")));
//				list.add(item);
//			}
//			c.close();
//		}
//		db.close();
//		return list;
//	}
//
//	public ListItem getItem(String title, ArrayList<ListItem> list1) {
//		ListItem item;
//		Iterator<ListItem> it = list1.iterator();
//
//		while (it.hasNext()) {
//
//			item = it.next();
//
//			if (item.getTitle().equals(title)) {
//
//				return item;
//			}
//		}
//		return null;
//
//	}
//}

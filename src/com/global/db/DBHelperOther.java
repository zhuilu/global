package com.global.db;
////package com.global.db;
//
//import java.util.ArrayList;
//
//import org.json.JSONArray;
//
//import com.global.util.UserInfo;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//public class DBHelper {
//    public static final String  TAG               = "Recent_DBHelper.java";
//
//    public static final int     aliUser_maxRows   = 5;
//    public static final int     loginUser_maxRows = 5;
//
//    private static final String DataBaseName      = "RecentDB";
//    private static final String TableName         = "RecentTable";         //原来的表
//    private static final String TableName2        = "RecentTable2";        //为了保证老用户可以使用新建一个表
//    SQLiteDatabase              db;
//    Context                     context;
//
//    public DBHelper(Context context) {
//        this.context = context;
//    }
//
//    public boolean deleteAll() {
//        String sql = "delete from " + DBHelper.TableName2;
//        try {
//            this.db.execSQL(sql);
//            return true;
//        } catch (Exception e) {
//            return false;
//        } finally {
//            // this.db.close();
//        }
//    }
//
//    public ArrayList<UserInfo> getAllLoginUsers(String type) {
//        ArrayList<UserInfo> allLoginUsers = new ArrayList<UserInfo>();
//        UserInfo userInfo = null;
//
//        Cursor cur;
//        if (type == null) {
//            cur = db.query(DBHelper.TableName2, new String[] { "ID", "NAME", "PASSWORD", "TYPE",
//                    "LOGINTIME", "USERID", "RSAPASSWORD", "RANDOMNUM", "REALNAME", "PHONENO",
//                    "USERAVTARPATH" }, null, null, null, null, "ID desc");
//        } else {
//            cur = db.query(DBHelper.TableName2, new String[] { "ID", "NAME", "PASSWORD",
//                    "LOGINTIME", "USERID", "RSAPASSWORD", "RANDOMNUM", "REALNAME", "PHONENO",
//                    "USERAVTARPATH" }, "TYPE = ?", new String[] { type }, null, null, "ID desc");
//        }
//        if (cur.getCount() < 0) {
//            return null;
//        } else {
//            while (cur.moveToNext()) {
//                userInfo = new UserInfo();
//                userInfo.userAccount = cur.getString(cur.getColumnIndex("NAME"));
//                userInfo.userPassword = cur.getString(cur.getColumnIndex("PASSWORD"));
//                if (type == null) {
//                    userInfo.type = cur.getString(cur.getColumnIndex("TYPE"));
//                } else
//                    userInfo.type = type;
//                userInfo.userId = cur.getString(cur.getColumnIndex("USERID"));
//                //add two column
//                userInfo.rsaPassword = cur.getString(cur.getColumnIndex("RSAPASSWORD"));
//                userInfo.randomNum = cur.getString(cur.getColumnIndex("RANDOMNUM"));
//                userInfo.userName = cur.getString(cur.getColumnIndex("REALNAME"));
//                userInfo.phoneNo = cur.getString(cur.getColumnIndex("PHONENO"));
//                userInfo.userAvtarPath = cur.getString(cur.getColumnIndex("USERAVTARPATH"));
//                allLoginUsers.add(userInfo);
//            }
//        }
//        cur.close();
//        return allLoginUsers;
//
//    }
//
//    /**
//     * 获取所有账户列表
//     */
//    public JSONArray getAllUser(String type) {
//        JSONArray jsonArray = null;
//
//        Cursor cur;
//        if (type == null) {
//            cur = db
//                .query(
//                    DBHelper.TableName2,
//                    new String[] { "ID", "NAME"/*, "PASSWORD", "TYPE", "LOGINTIME", "USERID","RSAPASSWORD","RANDOMNUM","REALNAME","PHONENO"*/},
//                    null, null, null, null, "ID desc");
//        } else {
//            cur = db
//                .query(
//                    DBHelper.TableName2,
//                    new String[] { "ID", "NAME"/*, "PASSWORD", "LOGINTIME", "USERID","RSAPASSWORD","RANDOMNUM","REALNAME","PHONENO"*/},
//                    "TYPE = ?", new String[] { type }, null, null, "ID desc");
//        }
//        if (cur.moveToFirst()) {
//            jsonArray = new JSONArray();
//            do {
//                String account = cur.getString(cur.getColumnIndex("NAME"));
//                jsonArray.put(account);
//            } while (cur.moveToNext());
//        }
//
//        cur.close();
//        return jsonArray;
//    }
//
//    public UserInfo getUser(String userName, String Type) {
//        UserInfo userInfo = null;
//
//        if (!db.isOpen())
//            return userInfo;
//        Cursor cur;
//        cur = db.query(DBHelper.TableName2, new String[] { "ID", "NAME", "PASSWORD", "USERID",
//                "RSAPASSWORD", "RANDOMNUM", "REALNAME", "PHONENO", "USERAVTARPATH" },
//            "NAME = ? AND TYPE = ?", new String[] { userName, Type }, null, null, "");
//
//        if (cur.moveToFirst()) {
//            userInfo = new UserInfo();
//            userInfo.userAccount = cur.getString(cur.getColumnIndex("NAME"));
//            userInfo.userPassword = cur.getString(cur.getColumnIndex("PASSWORD"));
//            userInfo.userId = cur.getString(cur.getColumnIndex("USERID"));
//
//            if (cur.getString(cur.getColumnIndex("RSAPASSWORD")) != null) {
//                userInfo.rsaPassword = cur.getString(cur.getColumnIndex("RSAPASSWORD"));
//            }
//
//            if (cur.getString(cur.getColumnIndex("RANDOMNUM")) != null) {
//                userInfo.randomNum = cur.getString(cur.getColumnIndex("RANDOMNUM"));
//            }
//
//            if (cur.getString(cur.getColumnIndex("REALNAME")) != null) {
//                userInfo.userName = cur.getString(cur.getColumnIndex("REALNAME"));
//            }
//
//            if (cur.getString(cur.getColumnIndex("PHONENO")) != null) {
//                userInfo.phoneNo = cur.getString(cur.getColumnIndex("PHONENO"));
//            }
//            if (cur.getString(cur.getColumnIndex("USERAVTARPATH")) != null) {
//                userInfo.userAvtarPath = cur.getString(cur.getColumnIndex("USERAVTARPATH"));
//            }
//
//        }
//
//        cur.close();
//
//        return userInfo;
//    }
//
//    public void open(Context context) {
//        if (null == db || !this.db.isOpen()) {
//            //			context.openDatabase(DataBaseName,null);
//            this.db = context.openOrCreateDatabase(DataBaseName, Context.MODE_PRIVATE, null);
//            Cursor cur = this.db.query(TableName2, null, null, null, null, null, "ID desc");
//
//            //			Log.v(this.TAG, "get column:"+cur.getColumnIndex("USERID"));
//            if (cur.getColumnIndex("USERID") == -1) {
//                this.db.execSQL("alter table  " + TableName2 + " add column" + " USERID" + " TEXT"
//                                + " ;");
//            }
//
//            //add two column to the new table. rasPassword,randomNum
//            if (cur.getColumnIndex("RSAPASSWORD") == -1) {
//                this.db.execSQL("ALTER TABLE " + TableName2 + " ADD COLUMN RSAPASSWORD TEXT;");
//            }
//            if (cur.getColumnIndex("RANDOMNUM") == -1) {
//                this.db.execSQL("ALTER TABLE " + TableName2 + " ADD COLUMN RANDOMNUM TEXT;");
//                //init randomNum for all column 
//            }
//
//            if (cur.getColumnIndex("REALNAME") == -1) {
//                this.db.execSQL("ALTER TABLE " + TableName2 + " ADD COLUMN REALNAME TEXT;");
//            }
//            if (cur.getColumnIndex("PHONENO") == -1) {
//                this.db.execSQL("ALTER TABLE " + TableName2 + " ADD COLUMN PHONENO TEXT;");
//            }
//            if (cur.getColumnIndex("USERAVTARPATH") == -1) {
//                this.db.execSQL("ALTER TABLE " + TableName2 + " ADD COLUMN USERAVTARPATH TEXT;");
//            }
//        }
//    }
//
//    public boolean isExitDatabase(Context context) {
//        if (null == db || !db.isOpen()) {
//            this.db = context.openOrCreateDatabase(DataBaseName, Context.MODE_PRIVATE, null);
//            Cursor cur = this.db.query(TableName2, null, null, null, null, null, "ID desc");
//            if (cur.getColumnIndex("USERID") == -1 || cur.getColumnIndex("RSAPASSWORD") == -1
//                || cur.getColumnIndex("RANDOMNUM") == -1 || cur.getColumnIndex("REALNAME") == -1
//                || cur.getColumnIndex("PHONENO") == -1 || cur.getColumnIndex("USERAVTARPATH") == -1) {
//                return false;
//            }
//            return true;
//        }
//        return true;
//    }
//
//    public void deleteDatabase(Context context) {
//        context.deleteDatabase(DataBaseName);
//    }
//
//    public void close() {
//        try {
//            if (db.isOpen())
//                db.close();
//        } catch (Exception e) {
//        }
//    }
//
//}

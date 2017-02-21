package com.lucious.presence.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ztx on 2017/2/9.
 */

public class DatabaseUtils extends SQLiteOpenHelper {
    private Context mContext;
    private static SQLiteDatabase db = null;
    private static DatabaseUtils dbHelper = null;

    public static synchronized DatabaseUtils getInstance(Context context) {
        if(dbHelper == null) {
            dbHelper = new DatabaseUtils(context,"Presence.db",null,1);
        }
        return dbHelper;
    }

    public DatabaseUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public DatabaseUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveJsonToDatabase(String tableName,String jsonString) {
        if(db == null) {
            db = dbHelper.getWritableDatabase();
        }
        if(!isTableExist(tableName)) {
            String tableExecsql = "create table " + tableName + " (" + tableName + " ("
                    + "id integer primary key autoincrement,"
                    + tableName + "Json" + " text)";
            db.execSQL(tableExecsql);
        }
        ContentValues values = new ContentValues();
        values.put(tableName + "Json",jsonString);
        db.insert(tableName,null,values);
        values.clear();
    }

    public String getJsonFromDatabase(String tableName) {
        if(db == null) {
            db = dbHelper.getWritableDatabase();
        }
        if(!isTableExist(tableName)) {
            String tableExecsql = "create table " + tableName + " ("
                    + "id integer primary key autoincrement,"
                    + tableName + "Json" + " text)";
            db.execSQL(tableExecsql);
        }
        Cursor cursor = db.query(tableName,null,null,null,null,null,null);
        String jsonString = "";
        if(cursor.moveToFirst()) {
            do {
                jsonString = cursor.getString(cursor.getColumnIndex(tableName + "Json"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return jsonString;
    }

    public void clearDatabaseTable(String tableName) {
        if(db == null) {
            db = dbHelper.getWritableDatabase();
        }
        db.delete(tableName,"id >= ?",new String[]{"0"});
    }

    public boolean isTableExist(String tabName) {
        boolean result = false;
        if(tabName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String sql = "select count(*) as c from sqlite_master where type = 'table' and name ='" + tabName.trim() +"' ";
            cursor = db.rawQuery(sql,null);
            if(cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if(count > 0) {
                    result = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

















}

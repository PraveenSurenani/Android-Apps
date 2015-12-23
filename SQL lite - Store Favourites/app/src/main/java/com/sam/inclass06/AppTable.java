package com.sam.inclass06;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/*
Sam Painter and Praveen Surenani
InClass07
 */
public class AppTable {
    static final String TABLE_NAME = "apps";
    static final String APP_ID = "appID";
    static final String APP_NAME = "appName";
    static final String DEVELOPER_NAME = "developerName";
    static final String RELEASE_DATE = "releaseDate";
    static final String PRICE = "price";
    static final String CATEGORY = "category";
    static final String IMAGE_URL = "imageURL";

    static public void onCreate(SQLiteDatabase db) {
        Log.d("demo", "running on create");
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + AppTable.TABLE_NAME + " (");
        sb.append(AppTable.APP_ID + " integer primary key autoincrement, ");
        sb.append(AppTable.APP_NAME + " text not null, ");
        sb.append(AppTable.DEVELOPER_NAME + " text not null, ");
        sb.append(AppTable.RELEASE_DATE + " text not null, ");
        sb.append(AppTable.PRICE + " text not null, ");
        sb.append(AppTable.CATEGORY + " text not null, ");
        sb.append(AppTable.IMAGE_URL + " text not null ");
        sb.append(");");
        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppTable.TABLE_NAME);
        AppTable.onCreate(db);
    }
    static public void delete(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + AppTable.TABLE_NAME);
    }
}
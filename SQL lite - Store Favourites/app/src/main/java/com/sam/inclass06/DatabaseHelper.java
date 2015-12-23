package com.sam.inclass06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
Sam Painter and Praveen Surenani
InClass07
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "apps.db";
    static final int DATABASE_VERSION = 4;

    DatabaseHelper(Context c) {
        super(c,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AppTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppTable.onUpgrade(db, oldVersion, newVersion);
    }
}

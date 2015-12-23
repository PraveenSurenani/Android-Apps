package com.sam.inclass06;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
Sam Painter and Praveen Surenani
InClass07
 */
public class AppDAO {
    private SQLiteDatabase db;
    public AppDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(App app) {
        ContentValues values = new ContentValues();
        values.put(AppTable.APP_NAME, app.getAppName());
        values.put(AppTable.DEVELOPER_NAME, app.getDeveloperName());
        values.put(AppTable.RELEASE_DATE, app.getReleaseDate());
        values.put(AppTable.PRICE, app.getPrice());
        values.put(AppTable.CATEGORY, app.getCategory());
        values.put(AppTable.IMAGE_URL, app.getImageURL());
        return db.insert(AppTable.TABLE_NAME, null, values);
    }

    public boolean update(App app) {
        ContentValues values = new ContentValues();
        values.put(AppTable.APP_NAME, app.getAppName());
        values.put(AppTable.DEVELOPER_NAME, app.getDeveloperName());
        values.put(AppTable.RELEASE_DATE, app.getReleaseDate());
        values.put(AppTable.PRICE, app.getPrice());
        values.put(AppTable.CATEGORY, app.getCategory());
        values.put(AppTable.IMAGE_URL, app.getImageURL());
        return db.update(AppTable.TABLE_NAME, values, AppTable.APP_ID + "=" + app.getAppID(), null)>0;
    }

    public boolean delete(App app) {
        return db.delete(AppTable.TABLE_NAME, AppTable.APP_ID + "=" + app.getAppID(), null)>0;
    }

    public App get(long id) {
        App app = null;
        Cursor c = db.query(true, AppTable.TABLE_NAME,
                new String[]{AppTable.APP_ID, AppTable.APP_NAME, AppTable.DEVELOPER_NAME, AppTable.RELEASE_DATE, AppTable.PRICE, AppTable.CATEGORY, AppTable.IMAGE_URL},
                AppTable.APP_ID+"="+ id, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            app = this.buildAppFromCursor(c);
        }
        if (!c.isClosed())
            c.close();
        return app;
    }

    public List<App> getAll() {
        List<App> list = new ArrayList<>();
        Cursor c = db.query(AppTable.TABLE_NAME,
                new String[]{AppTable.APP_ID, AppTable.APP_NAME, AppTable.DEVELOPER_NAME, AppTable.RELEASE_DATE, AppTable.PRICE, AppTable.CATEGORY, AppTable.IMAGE_URL},
                null, null, null, null, null);
        if (c!= null){
            c.moveToFirst();
            do {
                App app = this.buildAppFromCursor(c);
                if (app != null) {
                    list.add(app);
                }
            } while (c.moveToNext());
            if(!c.isClosed())
                c.close();
        }
        return list;
    }

    private App buildAppFromCursor(Cursor c) {
        App app = null;
        if (c != null) {
            app = new App();
            app.setAppID(c.getLong(0));
            app.setAppName(c.getString(1));
            app.setDeveloperName(c.getString(2));
            app.setReleaseDate(c.getString(3));
            app.setPrice(c.getString(4));
            app.setCategory(c.getString(5));
            app.setImageURL(c.getString(6));
        }
        return app;
    }
}

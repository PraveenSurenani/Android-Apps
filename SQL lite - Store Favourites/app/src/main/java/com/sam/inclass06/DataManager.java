package com.sam.inclass06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.List;

/*
Sam Painter and Praveen Surenani
InClass07
 */
public class DataManager {
    Context context;
    DatabaseHelper dbOpenHelper;
    SQLiteDatabase db;
    AppDAO appDAO;

    public DataManager(Context c) {
        this.context = c;
        dbOpenHelper = new DatabaseHelper(c);
        db = dbOpenHelper.getWritableDatabase();
        appDAO = new AppDAO(db);
    }

    public void close() {
        db.close();
    }
    public long saveApp(App app) {
        Toast.makeText(this.context, "Added app to database", Toast.LENGTH_SHORT).show();
        return appDAO.save(app);
    }

    public boolean updateApp(App app) {
        return appDAO.update(app);
    }

    public boolean deleteApp(App app) {
        Toast.makeText(this.context, "Deleted app from database", Toast.LENGTH_SHORT).show();
        return appDAO.delete(app);
    }

    public App getApp(long id) {
        return appDAO.get(id);
    }

    public List<App> getAllApps(){
        return appDAO.getAll();
    }

}

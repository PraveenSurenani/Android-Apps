package com.sam.inclass06;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
Sam Painter and Praveen Surenani
InClass07
 */
public class MainActivity extends AppCompatActivity implements GetAppList.IAppData {
    private ArrayList<App> apps;
    private ArrayList<App> displayedApps;
    private AppAdaptor adaptor;

    private static DataManager dm;

    @Override
    protected void onDestroy() {
        dm.close();
        AppTable.delete(dm.db);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites:
                ArrayList<App> favs = (ArrayList<App>) dm.getAllApps();
                setUp(favs);
                return true;
            case R.id.all:
                setUp(apps);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.apps_activity));
        dm = new DataManager(this);
        new GetAppList(this).execute(getString(R.string.api_url));
    }

    public void setUp(ArrayList<App> items) {
        ListView appList = (ListView)findViewById(R.id.appList);
        displayedApps = items;
        if (adaptor!= null) {
            adaptor.clear();
        }
        adaptor = new AppAdaptor(this, displayedApps);
        appList.setAdapter(adaptor);

        for (App a:displayedApps) {
            if (a.getFavorite()){
                View v = appList.getChildAt(displayedApps.indexOf(a));
                ImageView iv = (ImageView)v.findViewById(R.id.favorite);
                iv.setImageResource(android.R.drawable.btn_star_big_on);
            }
        }

        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App app = apps.get(position);
                String title = app.getAppName();
                String url = app.getImageURL();
                Boolean favorite = app.getFavorite();
                Intent previewIntent = new Intent(MainActivity.this, PreviewActivity.class);
                previewIntent.putExtra("title", title);
                previewIntent.putExtra("url", url);
                previewIntent.putExtra("favorite", favorite);
                startActivity(previewIntent);
            }
        });

        appList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView fav = (ImageView) view.findViewById(R.id.favorite);
                App app = apps.get(position);
                if(!app.getFavorite()) {
                    app.setFavorite(true);
                    fav.setImageResource(android.R.drawable.btn_star_big_on);
                    dm.saveApp(app);
                    Log.d("demo", app.toString());
                } else {
                    app.setFavorite(false);
                    fav.setImageResource(android.R.drawable.btn_star_big_off);
                    dm.deleteApp(app);
                }
                return true;
            }
        });
    }

    @Override
    public void setUpAppList(ArrayList<App> result) {
        apps = result;
        setUp(result);

    }

    @Override
    public Context getContext() {
        return this;
    }


}

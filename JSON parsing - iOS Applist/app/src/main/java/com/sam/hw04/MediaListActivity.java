package com.sam.hw04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
/*
Assignment: HW04
MediaListActivity.java
Samuel Painter and Praveen Surenari
 */
public class MediaListActivity extends AppCompatActivity {

    private LinearLayout container;
    private ArrayList<HashMap<String,String>> mediaItems;
    private String mediaType;
    private ProgressDialog pd;
    private SharedPreferences pref;

    private static final String PREF_NAME = "HW04PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        container = (LinearLayout)findViewById(R.id.mediaListContainer);
        Intent receivedIntent = getIntent();
        String url = receivedIntent.getStringExtra("url");
        mediaType = receivedIntent.getStringExtra("title");
        setTitle(mediaType);

        mediaItems = new ArrayList<HashMap<String, String>>();

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        Date lastTime = new Date(pref.getLong(mediaType + "time", 0));
        Date newDate = new Date(System.currentTimeMillis());
        int diffMinutes = (int) ( (newDate.getTime() - lastTime.getTime()) / (1000*60));

        if (diffMinutes < 2) {
            String storedCollection = pref.getString(mediaType, null);
            try {
                JSONArray array = new JSONArray(storedCollection);
                Log.d(MediaListActivity.class.toString(), array.toString());
                HashMap<String,String> item = null;
                for (int i = 0; i < array.length(); i ++){
                    JSONObject ary = array.getJSONObject(i);
                    Iterator<String> it = ary.keys();
                    item = new HashMap<String,String>();
                    while(it.hasNext()) {
                        String key = it.next();
                        item.put(key, (String)ary.get(key));
                    }
                    mediaItems.add(item);
                }
                setItems();
            } catch (JSONException e) {
                e.printStackTrace();
                mediaItems.clear();
                new GetMediaList().execute(url);
            }
        } else {
            mediaItems.clear();
            new GetMediaList().execute(url);
        }
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent detailIntent = new Intent(MediaListActivity.this, DetailedMediaActivity.class);
            HashMap<String,String> item = mediaItems.get(v.getId());
            detailIntent.putExtra("item_map", item);
            startActivity(detailIntent);
        }
    };

    private View.OnLongClickListener itemLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mediaItems.remove(container.indexOfChild(v));
            container.removeView(v);
            pref = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
            SharedPreferences.Editor ed = pref.edit();
            ed.remove(mediaType);
            ed.remove(mediaType + "time");
            JSONArray toCache = new JSONArray(mediaItems);
            ed.putString(mediaType, toCache.toString());
            ed.putLong(mediaType + "time", System.currentTimeMillis());
            ed.apply();

            return true;
        }
    };

    private void setItems() {
        for (int i = 0; i < mediaItems.size(); i++) {
            LinearLayout ll = new LinearLayout(MediaListActivity.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ImageView thumbnail = new ImageView(MediaListActivity.this);
            TextView tv = new TextView(MediaListActivity.this);
            ll.setId(i);
            ll.setMinimumHeight(70);
            Picasso.with(MediaListActivity.this).load(mediaItems.get(i).get("thumbnail")).resize(100,100).into(thumbnail);
            tv.setText(mediaItems.get(i).get("title"));
            ll.addView(thumbnail);
            ll.addView(tv);
            tv.setTextSize(24);
            ll.setOnClickListener(itemClickListener);
            ll.setLongClickable(true);
            ll.setOnLongClickListener(itemLongClickListener);
            container.addView(ll);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.setMargins(10, 10, 10, 10);
            ll.setLayoutParams(params);
        }
    }

    private class GetMediaList extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MediaListActivity.this);
            pd.setMessage("Retrieving list...");
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pref = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
            JSONArray toCache = new JSONArray(mediaItems);
            SharedPreferences.Editor ed = pref.edit();
            ed.remove(mediaType);
            ed.remove(mediaType + "time");
            ed.putString(mediaType, toCache.toString());
            ed.putLong(mediaType + "time", System.currentTimeMillis());
            ed.apply();


            setItems();
        }

        @Override
        protected Void doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONObject feed = jsonObject.getJSONObject("feed");
                JSONArray entries = feed.getJSONArray("entry");
                for (int i = 0; i < entries.length(); i++) {
                    JSONObject e = entries.getJSONObject(i);
                    HashMap<String, String> entry = new HashMap<>();

                    JSONObject priceObj = e.getJSONObject("im:price");
                    String price = priceObj.getString("label");
                    entry.put("price", price);

                    JSONArray imageArr = e.getJSONArray("im:image");
                    JSONObject thumbNailObj = imageArr.getJSONObject(0);
                    JSONObject imageObj = imageArr.getJSONObject(imageArr.length() - 1);
                    String thumbNailUrl = thumbNailObj.getString("label");
                    String imageUrl = imageObj.getString("label");
                    entry.put("thumbnail", thumbNailUrl);
                    entry.put("image", imageUrl);

                    JSONObject artistObj = e.getJSONObject("im:artist");
                    String artist = artistObj.getString("label");
                    entry.put("artist", artist);

                    JSONObject titleObj = e.getJSONObject("im:name");
                    String title = titleObj.getString("label");
                    entry.put("title", title);

                    JSONObject categoryObj = e.getJSONObject("category").getJSONObject("attributes");
                    String category = categoryObj.getString("label");
                    entry.put("category", category);

                    JSONObject releaseObj = e.getJSONObject("im:releaseDate").getJSONObject("attributes");
                    String releaseDate = releaseObj.getString("label");
                    entry.put("releaseDate", releaseDate);

                    JSONObject summaryObj;
                    if ((summaryObj = e.optJSONObject("summary")) != null) {
                        String summary;
                        if (!(summary = summaryObj.optString("label")).equals("")) {
                            entry.put("summary", summary);
                        }
                    }

                    JSONObject linkObj;
                    if ((linkObj = e.optJSONObject("link")) != null) {
                        String link = linkObj.getJSONObject("attributes").getString("href");
                        entry.put("link", link);
                    }

                    JSONArray linkArr;
                    if ((linkArr = e.optJSONArray("link")) != null) {
                        String link = linkArr.getJSONObject(0).getJSONObject("attributes").getString("href");
                        entry.put("link", link);
                        JSONObject durationObj = linkArr.getJSONObject(1).getJSONObject("im:duration");
                        String duration = durationObj.getString("label");
                        entry.put("duration", duration);
                    }

                    mediaItems.add(entry);

                }

            } catch (IOException|JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


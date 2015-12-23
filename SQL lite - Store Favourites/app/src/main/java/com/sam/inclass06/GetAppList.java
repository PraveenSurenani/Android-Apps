package com.sam.inclass06;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
/*
Sam Painter and Praveen Surenani
InClass07
 */
public class GetAppList extends AsyncTask<String, Void, ArrayList<App>> {

    IAppData activity;
    ProgressDialog progressDialog;

    public GetAppList(IAppData activity) {
        this.activity = activity;
    }

    public static interface IAppData {
        void setUpAppList(ArrayList<App> result);
        Context getContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity.getContext());
        progressDialog.setMessage("Fetching App List ...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        activity.setUpAppList(apps);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        super.onPostExecute(apps);
    }

    @Override
    protected ArrayList<App> doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        ArrayList<App> appList = new ArrayList<>();
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
                JSONObject entry = entries.getJSONObject(i);
                App app = new App();

                String appName = entry.getJSONObject("im:name").getString("label");
                app.setAppName(appName);

                JSONArray images = entry.getJSONArray("im:image");
                String appImage = images.getJSONObject(images.length() - 1).getString("label");
                app.setImageURL(appImage);

                String appPrice = entry.getJSONObject("im:price").getString("label");
                app.setPrice(appPrice);

                String devName = entry.getJSONObject("im:artist").getString("label");
                app.setDeveloperName(devName);

                String appCategory = entry.getJSONObject("category").getJSONObject("attributes").getString("label");
                app.setCategory(appCategory);

                String appDate = entry.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label");
                app.setReleaseDate(appDate);
                appList.add(app);
            }
            return appList;
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

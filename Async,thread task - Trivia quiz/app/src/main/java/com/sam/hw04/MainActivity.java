package com.sam.hw04;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * HW04
 * Sam Painter and Praveen Suenani
 * MainActivity.java
 */
public class MainActivity extends AppCompatActivity {
    private Button start;
    private Button create;
    private Button delete;
    private Button exit;
    private AlertDialog ad;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.buttonStart);
        create = (Button)findViewById(R.id.buttonCreate);
        delete = (Button)findViewById(R.id.buttonDelete);
        exit = (Button)findViewById(R.id.buttonExit);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Trivia.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQuestion.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle(getString(R.string.delete_alert_title));
                builder.setMessage(getString(R.string.delete_alert_message));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RequestParams params = new RequestParams("POST", getString(R.string.Delete_Api));
                        params.addParam("gid",getString(R.string.GID));
                        new DeleteQuestions().execute(params);
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                ad = builder.create();
                ad.show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private class DeleteQuestions extends AsyncTask<RequestParams,Void,Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                Toast.makeText(MainActivity.this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
            ad.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle(getString(R.string.progress_title));
            pd.setMessage(getString(R.string.progress_message));
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected Integer doInBackground(RequestParams... params) {
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0].baseUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params[0].getEncodedParams());
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                Integer ret = -1;
                while ((line = reader.readLine()) != null){
                    ret = Integer.parseInt(line);
                }
                return ret;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }
    }

}

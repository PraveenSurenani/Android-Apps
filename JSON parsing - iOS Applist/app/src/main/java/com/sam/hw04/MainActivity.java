package com.sam.hw04;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/*
Assignment: HW04
MainActivity.java
Samuel Painter and Praveen Surenari
 */

public class MainActivity extends AppCompatActivity {

    private String[] categories;
    private LinearLayout container;
    private String[] urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.main_title);

        container = (LinearLayout) findViewById(R.id.mediaOptionsContainer);
        categories = getResources().getStringArray(R.array.categories);
        urls = getResources().getStringArray(R.array.urls);

        for (String item:categories) {
            TextView tv = new TextView(this);
            tv.setText(item);
            tv.setOnClickListener(mediaClick);
            container.addView(tv);
            setTVStyle(tv);
        }

    }

    View.OnClickListener mediaClick = new View.OnClickListener() {
        public void onClick(View v) {
            TextView tv = (TextView) v;
            Intent mediaListIntent = new Intent(MainActivity.this, MediaListActivity.class);
            int index = container.indexOfChild(v);
            String url = urls[index];
            mediaListIntent.putExtra("url", url);
            mediaListIntent.putExtra("title",tv.getText().toString());
            startActivity(mediaListIntent);
        }
    };

    private void setTVStyle(TextView tv) {
        tv.setTextSize(42);
        tv.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv.getLayoutParams();
        params.setMargins(20, 20, 20, 20);
        tv.setLayoutParams(params);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

}

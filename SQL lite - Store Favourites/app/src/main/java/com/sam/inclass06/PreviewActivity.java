package com.sam.inclass06;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
/*
Sam Painter and Praveen Surenani
InClass07
 */
public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TextView appTitle = (TextView) findViewById(R.id.app_title);
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView fav = (ImageView) findViewById(R.id.preview_favorite);
        Intent receivedIntent = getIntent();
        String title = receivedIntent.getStringExtra("title");
        String url = receivedIntent.getStringExtra("url");
        Boolean favorite = receivedIntent.getBooleanExtra("favorite", false);
        appTitle.setText(title);
        Picasso.with(this).load(url).resize(400, 400).into(image);
        if (favorite.equals("true")) {
            Picasso.with(this).load(android.R.drawable.btn_star_big_on).into(fav);
        }
    }
}

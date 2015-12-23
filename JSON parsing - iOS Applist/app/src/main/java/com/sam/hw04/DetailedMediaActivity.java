package com.sam.hw04;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
/*
Assignment: HW04
DetailedMediActivity.java
Samuel Painter and Praveen Surenari
 */
public class DetailedMediaActivity extends AppCompatActivity {

    private LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_media);
        container = (LinearLayout)findViewById(R.id.detailed_container);
        Intent receivedIntent = getIntent();
        final HashMap<String,String> item_map = (HashMap<String,String>)receivedIntent.getExtras().get("item_map");
        setTitle(item_map.get("title"));
        TextView title = new TextView(DetailedMediaActivity.this);
        title.setText(item_map.get("title"));
        title.setTextSize(32);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        container.addView(title);

        if (item_map.containsKey("releaseDate")) {
            TextView releaseDate = new TextView(DetailedMediaActivity.this);
            releaseDate.setText(item_map.get("releaseDate"));
            releaseDate.setTextSize(28);
            releaseDate.setTypeface(null, Typeface.BOLD);
            releaseDate.setGravity(Gravity.CENTER_HORIZONTAL);
            container.addView(releaseDate);
        }

        ImageView imageView = new ImageView(DetailedMediaActivity.this);
        Picasso.with(DetailedMediaActivity.this).load(item_map.get("image")).resize(250,250).into(imageView);
        container.addView(imageView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(params);

        TextView artist = new TextView(DetailedMediaActivity.this);
        artist.setText("By: " + item_map.get("artist"));
        artist.setTextSize(28);
        artist.setTypeface(null, Typeface.BOLD);
        container.addView(artist);

        if (item_map.containsKey("duration")) {
            TextView duration = new TextView(DetailedMediaActivity.this);
            duration.setText("Duration: " + item_map.get("duration"));
            duration.setTypeface(null, Typeface.BOLD);
            duration.setTextSize(28);
            container.addView(duration);
        }

        if (item_map.containsKey("price")) {
            TextView price = new TextView(DetailedMediaActivity.this);
            String priceString = item_map.get("price");
            if (priceString.equals("Get")) {
                price.setText("Price: Free");
            } else {
                price.setText("Price: " + priceString);
            }
            price.setTextSize(28);
            price.setTypeface(null, Typeface.BOLD);
            container.addView(price);
        }

        if (item_map.containsKey("summary")) {
            TextView summary = new TextView(DetailedMediaActivity.this);
            summary.setText("Summary:");
            summary.setTextSize(28);
            summary.setTypeface(null, Typeface.BOLD);
            container.addView(summary);
            TextView summaryText = new TextView(DetailedMediaActivity.this);
            summaryText.setText(Html.fromHtml(item_map.get("summary")));
            summaryText.setTextSize(20);
            container.addView(summaryText);
        }

        if (item_map.containsKey("category")) {
            TextView category = new TextView(DetailedMediaActivity.this);
            category.setText("Category: " + item_map.get("category"));
            category.setTextSize(28);
            category.setTypeface(null, Typeface.BOLD);
            container.addView(category);

        }

        final TextView preview = new TextView(DetailedMediaActivity.this);
        preview.setText("Preview in Store:");
        preview.setTextSize(28);
        preview.setTypeface(null, Typeface.BOLD);
        container.addView(preview);

        TextView link = new TextView(DetailedMediaActivity.this);
        link.setText(item_map.get("link"));
        link.setTextSize(20);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setTextColor(Color.parseColor("#0645AD"));
        container.addView(link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previewIntent = new Intent(DetailedMediaActivity.this, PreviewActivity.class);
                previewIntent.putExtra("url", item_map.get("link"));
                previewIntent.putExtra("title", item_map.get("title"));
                startActivity(previewIntent);
            }
        });
    }

}

package com.sam.inclass06;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
/*
Sam Painter and Praveen Surenani
InClass07
 */
public class AppAdaptor extends ArrayAdapter<App>{
    Context context;
    ArrayList<App> appList;

    public AppAdaptor(Context context, ArrayList<App> items) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.appList = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.appThumbnail);
            holder.appName = (TextView) convertView.findViewById(R.id.app_name);
            holder.devName = (TextView) convertView.findViewById(R.id.dev_name);
            holder.releaseDate = (TextView) convertView.findViewById(R.id.release_date);
            holder.appPrice = (TextView) convertView.findViewById(R.id.price);
            holder.appCategory = (TextView) convertView.findViewById(R.id.category);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        ImageView thumbnail = holder.thumbnail;
        TextView appName = holder.appName;
        TextView devName = holder.devName;
        TextView releaseDate = holder.releaseDate;
        TextView appPrice = holder.appPrice;
        TextView appCategory = holder.appCategory;
        Picasso.with(this.context).load(appList.get(position).getImageURL()).into(thumbnail);
        appName.setText(appList.get(position).getAppName());
        devName.setText(appList.get(position).getDeveloperName());
        releaseDate.setText(appList.get(position).getReleaseDate());
        String price;
        if ((price = appList.get(position).getPrice()).equals("Get")) {
            appPrice.setText("Free");
        } else {
            appPrice.setText(price);
        }
        appCategory.setText(appList.get(position).getCategory());
        return convertView;
    }

    static class ViewHolder {
        ImageView thumbnail;
        TextView appName;
        TextView devName;
        TextView releaseDate;
        TextView appPrice;
        TextView appCategory;
    }

}

package com.sam.project;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.util.List;

/**
 * Created by sam on 12/13/15.
 */
public class ImageAdapter extends BaseAdapter {

    private List<ParseFile> images;
    private Context context;

    public ImageAdapter(Context c, List<ParseFile> images) {
        this.context = c;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            v = new View(context);
            v = i.inflate(R.layout.image_list_item,null);
            ParseImageView vh = (ParseImageView)v.findViewById(R.id.image_item);
            vh.setParseFile(images.get(position));
            vh.loadInBackground();
        } else {
            v = convertView;
        }
        return v;
    }

    public class Holder {
        ParseImageView vh;
    }
}

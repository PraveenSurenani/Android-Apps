package com.sam.project;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by sam on 12/13/15.
 */
public class InboxAdapter extends ParseQueryAdapter<ParseObject> {
    private Context context;

    public InboxAdapter(Context c) {
        super(c,  new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Message");
                query.whereEqualTo("to", ParseUser.getCurrentUser());
                query.include("_User");
                query.orderByDescending("createdAt");
                return query;
            }
        });
        this.context = c;
    }

    @Override
    public View getItemView(final ParseObject object, View v, final ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.inbox_item, null);
        }
        super.getItemView(object,v,parent);
        ParseImageView image = (ParseImageView)v.findViewById(R.id.read_or_unread);
        TextView fromT = (TextView)v.findViewById(R.id.inbox_from);
        TextView dateT = (TextView)v.findViewById(R.id.inbox_date);
        if(!object.getBoolean("read"))
            image.setImageResource(android.R.drawable.checkbox_off_background);
        else
            image.setImageResource(android.R.drawable.checkbox_on_background);

        ParseUser fromUser = object.getParseUser("from");
        try {
            fromUser.fetch();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("debug", fromUser.getObjectId() + " " + fromUser.toString());
        fromT.setText(fromUser.getString("first") + " " + fromUser.getString("last"));
        dateT.setText(object.getCreatedAt().toString());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Message.class);
                i.putExtra("message", object.getObjectId());
                context.startActivity(i);
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                object.deleteInBackground();
                parent.removeViewInLayout(v);
                parent.refreshDrawableState();
                return true;
            }
        });

        return v;
    }

}

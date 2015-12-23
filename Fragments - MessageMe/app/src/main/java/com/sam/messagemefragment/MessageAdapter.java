package com.sam.messagemefragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
/*
Sam Painter and Praveen Surenani
InClass09
MessageMeAdapter.java
 */
public class MessageAdapter extends ParseQueryAdapter<Message> {

    public MessageAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Message>() {
            public ParseQuery<Message> create() {
                ParseQuery query = new ParseQuery("Message");
                query.whereExists("message");
                query.include("_User");
                return query;
            }
        });

    }

    @Override
    public View getItemView(final Message object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(),R.layout.list_item, null);
        }
        super.getItemView(object,v,parent);

        TextView name = (TextView)v.findViewById(R.id.itemname);
        TextView message = (TextView)v.findViewById(R.id.itemmessage);
        TextView date = (TextView)v.findViewById(R.id.itemdate);
        ImageView delete = (ImageView)v.findViewById(R.id.itemdelete);

        String authorName = "";
        ParseUser user = object.getAuthor();
        try {
            authorName = user.fetch().getString("name");
        } catch (ParseException e){
            e.printStackTrace();
        }

        name.setText(authorName);
        message.setText(object.getMessage());
        date.setText(object.getDate().toString());

        if(object.getAuthor().equals(ParseUser.getCurrentUser())) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    object.deleteInBackground();
                    Messages.mAdapter.loadObjects();
                }
            });
        } else {
            delete.setVisibility(View.INVISIBLE);
        }
        return v;
    }
}


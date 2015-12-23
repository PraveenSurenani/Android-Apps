package com.sam.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by sam on 12/13/15.
 */
public class PublicUsersAdapter extends ParseQueryAdapter<ParseUser> {
    private Context context;

    public PublicUsersAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {
           public ParseQuery<ParseUser> create() {
               ParseQuery<ParseUser> query = ParseUser.getQuery();
               query.whereEqualTo("list", true);
               Log.d("demo", query.toString() );
               return query;
           }
        });
        this.context = context;
    }

    @Override
    public View getItemView(final ParseUser user, View v, ViewGroup parent){
        if(v == null) {
            v = View.inflate(getContext(), R.layout.list_item, null);
        }
        super.getItemView(user,v,parent);
        ParseImageView image = (ParseImageView)v.findViewById(R.id.user_image);
        ParseFile file = user.getParseFile("image");
        if(file != null){
            image.setParseFile(file);
            image.loadInBackground();
        }

        TextView name = (TextView)v.findViewById(R.id.user_name);
        name.setText(user.getString("first") + " " + user.getString("last"));
        Log.d("demo", "Putting user in list");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, User.class);
                i.putExtra("user", user.getObjectId());
                context.startActivity(i);
            }
        });
        return v;
    }
}

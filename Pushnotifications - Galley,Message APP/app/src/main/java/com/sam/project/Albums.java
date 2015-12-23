package com.sam.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Albums extends AppCompatActivity {

    private ListView listView;
    private ParseQueryAdapter<ParseObject> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        listView = (ListView)findViewById(R.id.listview_albums);
        adapter = new ParseQueryAdapter<>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<>("Album");
                query.whereEqualTo("private",false);

                ParseQuery<ParseObject> owner = new ParseQuery<>("Album");
                owner.whereEqualTo("owner", ParseUser.getCurrentUser());

                ParseQuery<ParseObject> invited = new ParseQuery<ParseObject>("Album");
                invited.whereEqualTo("invited_users", ParseUser.getCurrentUser());

                List<ParseQuery<ParseObject>> queries = new ArrayList<>();
                queries.add(query);
                queries.add(owner);
                queries.add(invited);
                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                return mainQuery;
            }
        });
        adapter.setTextKey("name");
        adapter.setImageKey("titleImage");

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject album = (ParseObject)parent.getItemAtPosition(position);
                Intent i = new Intent(Albums.this, Album.class);
                i.putExtra("album", album.getObjectId());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.loadObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent i = new Intent(this, Profile.class);
            startActivity(i);
        }

        if (id == R.id.logout) {
            ParseUser.logOutInBackground();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        if (id == R.id.albums) {
            Intent i = new Intent(this, Albums.class);
            startActivity(i);
        }

        if (id == R.id.users) {
            Intent i = new Intent(this, Users.class);
            startActivity(i);
        }


        if (id == R.id.create_album) {
            Intent i = new Intent(this, CreateAlbum.class);
            startActivity(i);
        }

        if (id == R.id.create_message) {
            Intent i = new Intent(this, CreateMessage.class);
            startActivity(i);
        }

        if (id == R.id.inbox) {
            Intent i = new Intent(this, Inbox.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}

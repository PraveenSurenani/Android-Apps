package com.sam.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class Inbox extends AppCompatActivity {

    private ListView listView;
    private InboxAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.loadObjects();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        listView = (ListView)findViewById(R.id.listview_inbox);

        adapter = new InboxAdapter(this);

        listView.setAdapter(adapter);

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

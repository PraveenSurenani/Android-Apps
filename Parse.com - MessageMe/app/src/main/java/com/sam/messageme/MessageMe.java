package com.sam.messageme;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/*
Sam Painter and Praveen Surenari
InClass8
 */

public class MessageMe extends AppCompatActivity {

    private ParseQueryAdapter<Message> adapter;
    private ListView listview;
    public static MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_me);
        listview = (ListView) findViewById(R.id.listview);

        adapter = new ParseQueryAdapter<Message>(this, "Message");

        adapter.setTextKey("message");

        mAdapter = new MessageAdapter(this);
        listview.setAdapter(mAdapter);
        mAdapter.loadObjects();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.loadObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                update();
                break;
            }
            case R.id.compose: {
                compose();
                break;
            }
            case R.id.logout: {
                ParseUser.logOutInBackground();
                Intent intent = new Intent(MessageMe.this, Login.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void update() {
        mAdapter.loadObjects();
        listview.setAdapter(mAdapter);
//        adapter.loadObjects();
//        listview.setAdapter(adapter);
    }

    private void compose() {
        Intent intent = new Intent(MessageMe.this, Compose.class);
        startActivity(intent);
    }
}

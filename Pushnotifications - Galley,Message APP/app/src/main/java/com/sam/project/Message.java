package com.sam.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;

public class Message extends AppCompatActivity {

    private TextView from, message;
    private ParseImageView image;
    private Button reply;
    private ParseObject mess;
    private Boolean isApproveRequest;
    private Button approve;
    private ParseUser fromUser;
    private ParseObject albumToInsert;
    private ParseFile imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        from = (TextView)findViewById(R.id.from_message);
        message = (TextView)findViewById(R.id.message_content);
        image = (ParseImageView)findViewById(R.id.message_image);
        reply = (Button)findViewById(R.id.reply);
        isApproveRequest = false;
        approve = (Button)findViewById(R.id.approve);

        Intent i = getIntent();
        final String messageId = i.getStringExtra("message");
        try{
            ParseQuery<ParseObject> q = new ParseQuery<ParseObject>("Message");
            mess = q.get(messageId);
            String fromId = mess.getString("from");
//            ParseUser fromUser = ParseUser.getQuery().get(fromId);
//            from.setText(fromUser.getString("first") + " " + fromUser.getString("last"));
            fromUser = mess.getParseUser("from");
            fromUser.fetch();
            from.setText(fromUser.getString("first") + " " + fromUser.getString("last"));
            message.setText(mess.getString("message"));
            isApproveRequest = mess.getBoolean("approveRequest");
            albumToInsert = mess.getParseObject("album");
            imageFile = mess.getParseFile("image");
            image.setParseFile(imageFile);
            image.loadInBackground();
            mess.put("read", true);
            mess.saveInBackground();
        } catch(ParseException e){
            e.printStackTrace();
        }

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Message.this, CreateMessage.class);
                startActivity(i);
            }
        });

        if(isApproveRequest != null) {
            if(isApproveRequest){
                approve.setVisibility(View.VISIBLE);
                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ParseObject aMessage = new ParseObject("Message");
                        aMessage.put("approveRequest",false);
                        aMessage.put("from", ParseUser.getCurrentUser());
                        aMessage.put("message", "Photo approved");
                        aMessage.put("to", fromUser);
                        aMessage.put("read", false);
                        aMessage.saveInBackground();
                        Log.d("debgu", albumToInsert.getObjectId());
                        albumToInsert.add("images", imageFile);
                        albumToInsert.saveInBackground();
                        mess.deleteInBackground();
                        ParsePush push = new ParsePush();
                        push.setChannel(fromUser.getObjectId());
                        String n = ParseUser.getCurrentUser().getString("first") + " " + ParseUser.getCurrentUser().getString("last");
                        push.setMessage("You have an approval notice from: " + n);
                        push.sendInBackground();
                        finish();
                    }
                });
            }
        }
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

package com.sam.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CreateMessage extends AppCompatActivity {

    private ParseImageView image;
    private EditText message;
    private Button toButton;
    private Boolean imageSet;

    private List<String> userNames;
    private List<ParseUser> queriedUsers;
    private ParseObject messageObject;
    private ParseFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        imageSet = false;

        image = (ParseImageView)findViewById(R.id.create_message_image);
        message = (EditText)findViewById(R.id.create_message_text);
        toButton = (Button)findViewById(R.id.create_message_send_button);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });

        userNames = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("notify", true);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    queriedUsers = users;
                    userNames = new ArrayList<String>();
                    for (ParseUser user : users) {
                        userNames.add(user.getString("first") + " " + user.getString("last"));
                    }
                    toButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String messageText = message.getText().toString();
                            if(messageText.length() == 0) {
                                Toast.makeText(CreateMessage.this, "Must have message text", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateMessage.this);
                            builder.setTitle("Select User to send Message to");
                            builder.setItems(userNames.toArray(new CharSequence[userNames.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {
                                    messageObject = new ParseObject("Message");
                                    messageObject.put("read", false);
                                    messageObject.put("to", queriedUsers.get(which));
                                    messageObject.put("from", ParseUser.getCurrentUser());
                                    messageObject.put("approveRequest", false);
                                    messageObject.put("message", messageText);
                                    if(imageSet) {
                                        image.buildDrawingCache();
                                        Bitmap bmp = image.getDrawingCache();
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        byte[] image = stream.toByteArray();
                                        file = new ParseFile("image.jpeg", image);
                                        file.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                messageObject.put("image", file);
                                                messageObject.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        ParsePush push = new ParsePush();
                                                        push.setChannel(queriedUsers.get(which).getObjectId());
                                                        String n = ParseUser.getCurrentUser().getString("first") + " " + ParseUser.getCurrentUser().getString("last");
                                                        push.setMessage("You have a new message from: " + n);
                                                        push.sendInBackground();
                                                        CreateMessage.this.finish();
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        messageObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                ParsePush push = new ParsePush();
                                                push.setChannel(queriedUsers.get(which).getObjectId());
                                                String n = ParseUser.getCurrentUser().getString("first") + " " + ParseUser.getCurrentUser().getString("last");
                                                push.setMessage("You have a new message from: " + n);
                                                push.sendInBackground();
                                                CreateMessage.this.finish();
                                            }
                                        });
                                    }
                                }
                            });
                            builder.create().show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageSet = true;
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

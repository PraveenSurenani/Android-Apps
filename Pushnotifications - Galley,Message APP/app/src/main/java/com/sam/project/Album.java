package com.sam.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Album extends AppCompatActivity {


    private GridView gridView;
    private ImageAdapter imageAdapter;
    private Button invite;
    private TextView plabel;
    private Button add;
    private Button invited_users;
    private Button delete;
    private Boolean isOwner;

    private ParseObject album;
    private List<String> userNames;
    private List<ParseUser> queriedUsers;
    private ParseFile file;
    private List<ParseUser> invitedUserList;
    private List<ParseFile> images;
    private ParseUser owner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        invite = (Button)findViewById(R.id.invite_users);
        invited_users = (Button)findViewById(R.id.invited_users);
        add = (Button)findViewById(R.id.add_image);
        plabel = (TextView)findViewById(R.id.privatelabel);
        delete = (Button)findViewById(R.id.delete_album);

        isOwner = false;
        images = new ArrayList<>();
        List<ParseUser> invitedUsers = new ArrayList<>();
        owner = new ParseUser();

        Intent i = getIntent();
        final String albumId = i.getStringExtra("album");
        try {
            ParseQuery<ParseObject> q = new ParseQuery<ParseObject>("Album");
            album = q.get(albumId);
            images = album.getList("images");
            invitedUsers = album.getList("invited_users");
            owner = album.getParseUser("owner");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(album.getBoolean("private")) {
            plabel.setText("Private");
        } else {
            plabel.setText("Public");
        }
        gridView = (GridView)findViewById(R.id.gridview_album);
        imageAdapter = new ImageAdapter(this, images);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Album.this);
                LayoutInflater inflater = (LayoutInflater) Album.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View vh = inflater.inflate(R.layout.image_list_item, null);
                ParseImageView im = (ParseImageView) vh.findViewById(R.id.image_item);
                im.setParseFile(images.get(position));
                im.loadInBackground();
                builder.setView(vh);
                builder.setCancelable(true);
                builder.create().show();
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        add.setVisibility(View.INVISIBLE);
        invite.setVisibility(View.INVISIBLE);
        invited_users.setVisibility(View.INVISIBLE);
        if (invitedUsers == null) {
            invitedUsers = new ArrayList<>();
            invitedUsers.add(owner);
        }
        List<String> invitedStrings = new ArrayList<>();
        for (ParseUser user: invitedUsers) {
            invitedStrings.add(user.getObjectId());
        }

        if(currentUser.getObjectId().equals(owner.getObjectId())) {
            isOwner = true;
            userNames = new ArrayList<>();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("list", true);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null) {
                        queriedUsers = users;
                        userNames = new ArrayList<String>();
                        for (ParseUser user : users) {
                            userNames.add(user.getString("first") + " " + user.getString("last"));
                        }
                        invite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Album.this);
                                builder.setTitle("Select User to invite");
                                builder.setItems(userNames.toArray(new CharSequence[userNames.size()]), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        album.add("invited_users", queriedUsers.get(which));
                                        album.saveInBackground();
                                        ParsePush push = new ParsePush();
                                        push.setChannel(queriedUsers.get(which).getObjectId());
                                        String n = ParseUser.getCurrentUser().getString("first") + " " + ParseUser.getCurrentUser().getString("last");
                                        push.setMessage(n + " has shared an album with you.");
                                        push.sendInBackground();
                                    }
                                });
                                builder.create().show();
                            }
                        });
                    }
                }
            });

            invited_users.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitedUserList = album.getList("invited_users");
                    if (invitedUserList == null) {
                        return;
                    }
                    List<String> invitedStrings = new ArrayList<String>();
                    for (ParseUser user : invitedUserList) {
                        try {
                            user.fetch();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        invitedStrings.add(user.getString("first") + " " + user.getString("last"));
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Album.this);
                    builder.setTitle("Invited Users");
                    builder.setItems(invitedStrings.toArray(new CharSequence[invitedStrings.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            });

            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    List<ParseFile> toRemove = new ArrayList<ParseFile>();
                    ParseFile tr = images.get(position);
                    toRemove.add(tr);
                    album.removeAll("images", toRemove);
                    album.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            images = album.getList("images");
                            imageAdapter = new ImageAdapter(Album.this, images);
                            gridView.setAdapter(imageAdapter);
                        }
                    });
                    return true;
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    album.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            Album.this.finish();
                        }
                    });
                }
            });

            invite.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            invited_users.setVisibility(View.VISIBLE);
        } else {
            ((ViewGroup) invite.getParent()).removeView(invite);
            ((ViewGroup) invited_users.getParent()).removeView(invited_users);
            ((ViewGroup) delete.getParent()).removeView(delete);
        }
        if(currentUser.getObjectId().equals(owner.getObjectId()) || invitedStrings.contains(currentUser.getObjectId())) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 0);
                }
            });

            add.setVisibility(View.VISIBLE);
        } else {
            ((ViewGroup) add.getParent()).removeView(add);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();
                file = new ParseFile("image.jpeg", image);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (isOwner) {
                            album.add("images", file);
                            album.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    images = album.getList("images");
                                    imageAdapter = new ImageAdapter(Album.this, images);
                                    gridView.setAdapter(imageAdapter);
                                }
                            });
                        } else {
                            ParseObject message = new ParseObject("Message");
                            message.put("from", ParseUser.getCurrentUser());
                            message.put("to", owner);
                            message.put("read", false);
                            message.put("album", album);
                            message.put("image", file);
                            message.put("message", "Please approve this image.");
                            message.put("approveRequest", true);
                            message.saveInBackground();
                            ParsePush push = new ParsePush();
                            push.setChannel(owner.getObjectId());
                            String n = ParseUser.getCurrentUser().getString("first") + " " + ParseUser.getCurrentUser().getString("last");
                            push.setMessage("You have an approve request from: " + n);
                            push.sendInBackground();
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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

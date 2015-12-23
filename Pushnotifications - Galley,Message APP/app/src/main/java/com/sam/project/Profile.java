package com.sam.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class Profile extends AppCompatActivity {

    private ImageView profileImage;
    private ParseUser current;
    private EditText first;
    private EditText last;
    private EditText gender;
    private Boolean imageChanged;
    private Switch list;
    private Switch notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageChanged = false;

        first = (EditText)findViewById(R.id.first_edit);
        last = (EditText)findViewById(R.id.last_edit);
        gender = (EditText)findViewById(R.id.gender_edit);
        profileImage = (ImageView)findViewById(R.id.profile_image);
        Button save = (Button)findViewById(R.id.save_profile);
        list = (Switch)findViewById(R.id.profile_list);
        notify = (Switch)findViewById(R.id.profile_notify);

        current = ParseUser.getCurrentUser();

        if(current != null) {
            first.setText(current.getString("first"));
            last.setText(current.getString("last"));
            gender.setText(current.getString("gender"));
            if (current.getBoolean("list"))
                list.setChecked(true);
            else
                list.setChecked(false);
            if (current.getBoolean("notify"))
                notify.setChecked(true);
            else
                notify.setChecked(false);

            ParseFile image = (ParseFile)current.get("image");
            if(image != null) {
                image.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            profileImage.setImageBitmap(bmp);
                        }
                    }
                });
            }
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.put("first", first.getText().toString());
                current.put("last", last.getText().toString());
                current.put("gender", gender.getText().toString());
                current.put("list", list.isChecked());
                current.put("notify", notify.isChecked());
                if(imageChanged) {
                    profileImage.buildDrawingCache();
                    Bitmap bmp = profileImage.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] image = stream.toByteArray();
                    ParseFile file = new ParseFile("profile.jpeg", image);
                    file.saveInBackground();
                    current.put("image", file);
                }
                current.saveInBackground();
                if(ParseUser.getCurrentUser().getBoolean("notify")) {
                    ParsePush.subscribeInBackground("all");
                    ParsePush.subscribeInBackground(ParseUser.getCurrentUser().getObjectId());
                } else {
                    ParsePush.unsubscribeInBackground("all");
                    ParsePush.unsubscribeInBackground(ParseUser.getCurrentUser().getObjectId());
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
                profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageChanged = true;
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

package com.sam.project;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class Signup extends AppCompatActivity {
    private EditText first;
    private EditText last;
    private ImageView profileImage;

    private EditText email;
    private EditText password;
    private EditText passConfirm;
    private EditText gender;
    private ParseUser user;
    private ParseFile file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        first = (EditText) findViewById(R.id.first_sign);
        last = (EditText) findViewById(R.id.last_sign);
        email = (EditText) findViewById(R.id.email_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        passConfirm = (EditText) findViewById(R.id.password_again_edit_text);
        gender = (EditText) findViewById(R.id.gender_sign);
        profileImage = (ImageView) findViewById(R.id.profile_image_signup);

        Button cancel = (Button) findViewById(R.id.cancel);
        Button signup = (Button) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup.this.finish();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
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
        }
    }
    private void signup() {
        String firstText = first.getText().toString().trim();
        String lastText = last.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String passwordAgainText = passConfirm.getText().toString().trim();
        String genderText = gender.getText().toString().trim();

        // Validate the sign up data
        boolean validationError = false;
        if (firstText.length() == 0) {
            validationError = true;
        }
        if (lastText.length() == 0) {
            validationError = true;
        }

        if (emailText.length() == 0) {
            validationError = true;
        }
        if (passwordText.length() == 0) {

            validationError = true;
        }
        if (!passwordText.equals(passwordAgainText)) {

            validationError = true;
        }
        if (genderText.length() == 0) {

            validationError = true;
        }

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(Signup.this, "Validation Error", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(Signup.this);
        dialog.setMessage("Signing up...");
        dialog.show();

        // Set up a new Parse user
        user = new ParseUser();
        user.setUsername(emailText);
        user.setEmail(emailText);
        user.put("first", firstText);
        user.put("last", lastText);
        user.put("gender", genderText);
        user.put("list", true);
        user.put("notify", true);
        user.setPassword(passwordText);

        profileImage.buildDrawingCache();
        Bitmap bmp = profileImage.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        file = new ParseFile("profile.jpeg", image);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                user.put("image", file);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Toast.makeText(Signup.this, "Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Signup.this, Users.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            if(ParseUser.getCurrentUser().getBoolean("notify")) {
                                ParsePush.subscribeInBackground("all");
                                ParsePush.subscribeInBackground(ParseUser.getCurrentUser().getObjectId());
                            }
                            ParsePush push = new ParsePush();
                            push.setChannel("all");
                            push.setMessage("A new user has signed up");
                            push.sendInBackground();
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }
}

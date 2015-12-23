package com.sam.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser current = ParseUser.getCurrentUser();
        if (current != null) {
            Intent intent = new Intent(Login.this, Users.class);
            startActivity(intent);
        }

        email = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Button newUser = (Button) findViewById(R.id.new_user_button);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        Button fbLogin = (Button) findViewById(R.id.facebook_login);
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLogin();
            }
        });

        Button twLogin = (Button) findViewById(R.id.twitter_login);
        twLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twLogin();
            }
        });

    }

    private void twLogin() {
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                    ParseUser current = ParseUser.getCurrentUser();
                    if (current != null) {
                        setUpInstall();
                        Intent intent = new Intent(Login.this, Users.class);
                        startActivity(intent);
                    }
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Twitter!");
                    ParseUser current = ParseUser.getCurrentUser();
                    if (current != null) {
                        setUpInstall();
                        ParsePush push = new ParsePush();
                        push.setChannel("all");
                        push.setMessage("A new user has signed up");
                        push.sendInBackground();
                        Intent intent = new Intent(Login.this, Users.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d("MyApp", "User logged in through Twitter!");
                    ParseUser current = ParseUser.getCurrentUser();
                    if (current != null) {
                        setUpInstall();
                        Intent intent = new Intent(Login.this, Users.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void setUpInstall() {
        if(ParseUser.getCurrentUser().getBoolean("notify")) {
            ParsePush.subscribeInBackground("all");
            ParsePush.subscribeInBackground(ParseUser.getCurrentUser().getObjectId());
        }
    }

    private void fbLogin() {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                    ParseUser current = ParseUser.getCurrentUser();
                    if (current != null) {
                        setUpInstall();
                        Intent intent = new Intent(Login.this, Users.class);
                        startActivity(intent);
                    }
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    ParseUser current = ParseUser.getCurrentUser();
                    if (current != null) {
                        setUpInstall();
                        ParsePush push = new ParsePush();
                        push.setChannel("all");
                        push.setMessage("A new user has signed up");
                        push.sendInBackground();
                        Intent intent = new Intent(Login.this, Users.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    ParseUser current = ParseUser.getCurrentUser();
                    if (current != null) {
                        setUpInstall();
                        Intent intent = new Intent(Login.this, Users.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void login() {
        String emailString = email.getText().toString().trim();
        String password = pass.getText().toString().trim();

        // Validate the log in data
        boolean validationError = false;
        if (emailString.length() == 0) {
            validationError = true;
        }
        if (password.length() == 0) {
            validationError = true;
        }

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(Login.this, "Must have email and password.", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(Login.this);
        dialog.setMessage("Logging in...");
        dialog.show();
        // Call the Parse login method
        ParseUser.logInInBackground(emailString, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    setUpInstall();
                    Intent intent = new Intent(Login.this, Users.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}

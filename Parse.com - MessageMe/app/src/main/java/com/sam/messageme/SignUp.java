package com.sam.messageme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
/*
Sam Painter and Praveen Surenari
InClass8
 */
public class SignUp extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText passConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        passConfirm = (EditText) findViewById(R.id.password_again_edit_text);

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
                SignUp.this.finish();
            }
        });
    }

    private void signup() {
        String nameText = name.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String passwordAgainText = passConfirm.getText().toString().trim();

        // Validate the sign up data
        boolean validationError = false;
        if (nameText.length() == 0) {
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

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(SignUp.this, "Validation Error", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(SignUp.this);
        dialog.setMessage("Signing up...");
        dialog.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(emailText);
        user.setEmail(emailText);
        user.put("name", nameText);
        user.setPassword(passwordText);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Toast.makeText(SignUp.this, "Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUp.this, MessageMe.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
}

package com.sam.contactsdirectory;
/**
 * HW 2
 * CreateContact.java
 * Samuel Painter and Praveen Surenani
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateContact extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 90;

    private String imageString;
    private EditText name;
    private EditText number;
    private EditText email;
    private ImageView photo;
    private Button save;
    private Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.email);
        photo = (ImageView) findViewById(R.id.photo);
        save = (Button) findViewById(R.id.create_contact_save);
        cancel = (Button) findViewById(R.id.create_contact_cancel);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact_name = name.getText().toString();
                String contact_number = number.getText().toString();
                String contact_email = email.getText().toString();
                Contact newContact;

                if (contact_name == "" || contact_number == "" || contact_email == "") {
                    Toast.makeText(CreateContact.this, "You have missing information", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (contact_number.length() != 10) {
                        Toast.makeText(CreateContact.this, "Numbers have length 10", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (imageString == "" || imageString == null) {
                        newContact = new Contact(contact_name,contact_number,contact_email);
                    } else {
                        Uri contact_photo = Uri.parse(imageString);
                        newContact = new Contact(contact_name,contact_number,contact_email,contact_photo);
                    }
                }
                Intent intent = getIntent();
                intent.putExtra("contact", newContact);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selected = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selected, filePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                imageString = cursor.getString(columnIndex);
                cursor.close();
                photo.setImageBitmap(BitmapFactory.decodeFile(imageString));
            } else {
                Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something has gone wrong", Toast.LENGTH_LONG).show();
        }
    }

}

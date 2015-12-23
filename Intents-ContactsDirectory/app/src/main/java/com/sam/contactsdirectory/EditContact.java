package com.sam.contactsdirectory;

/**
 * HW 2
 * EditContact.java
 * Samuel Painter and Praveen Surenani
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EditContact extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 91;

    private String imageString;
    private Button select_contacts;
    private EditText name;
    private EditText number;
    private EditText email;
    private ImageView photo;
    private Button save;
    private Button cancel;
    private List<Contact> contacts;
    private Contact selected;
    private Contact edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        select_contacts = (Button) findViewById(R.id.select_contact);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.email);
        photo = (ImageView) findViewById(R.id.photo);
        save = (Button) findViewById(R.id.create_contact_save);
        cancel = (Button) findViewById(R.id.create_contact_cancel);

        Intent intent = getIntent();
        contacts = intent.getParcelableArrayListExtra("contacts");
        Collections.sort(contacts);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        select_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditContact.this);
                builder.setTitle(R.string.choose);
                List<String> contact_names = new ArrayList<String>();
                Iterator<Contact> iterator = contacts.iterator();
                while(iterator.hasNext()) {
                    contact_names.add(iterator.next().getName());
                }
                final CharSequence[] cont_names = contact_names.toArray(new String[contact_names.size()]);
                builder.setItems(cont_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = contacts.get(which);
                        edited = contacts.get(which);
                        name.setText(selected.getName());
                        number.setText(selected.getNumber());
                        email.setText(selected.getEmail());
                        if (selected.getPhoto() != null)
                            photo.setImageBitmap(BitmapFactory.decodeFile(selected.getPhoto().toString()));
                        else
                            photo.setImageResource(R.drawable.default_photo);

                    }
                });
                builder.create().show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact_name = name.getText().toString();
                String contact_number = number.getText().toString();
                String contact_email = email.getText().toString();
                Intent intent = getIntent();

                if (contact_name == "" || contact_number == "" || contact_email == "") {
                    Toast.makeText(EditContact.this, "You have missing information", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (contact_number.length() != 10) {
                        Toast.makeText(EditContact.this, "Numbers have length 10", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (imageString == "" || imageString == null) {
                        edited = new Contact(contact_name,contact_number,contact_email);
                    } else {
                        Uri contact_photo = Uri.parse(imageString);
                        edited = new Contact(contact_name,contact_number,contact_email,contact_photo);
                    }
                }
                if(edited.equals(selected)) {
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                } else {
                    Contact[] results = {selected, edited};
                    intent.putExtra("edited", results);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
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

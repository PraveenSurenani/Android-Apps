package com.sam.contactsdirectory;

/**
 * HW 2
 * DeleteContact.java
 * Samuel Painter and Praveen Surenani
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DeleteContact extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 92;

    private Button select_contacts;
    private EditText name;
    private EditText number;
    private EditText email;
    private ImageView photo;
    private Button cancel;
    private Button delete;
    private List<Contact> contacts;
    private Contact selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        select_contacts = (Button) findViewById(R.id.select_contact);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.email);
        photo = (ImageView) findViewById(R.id.photo);
        delete = (Button) findViewById(R.id.delete_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        Intent intent = getIntent();
        contacts = intent.getParcelableArrayListExtra("contacts");
        Collections.sort(contacts);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == null) {
                    Toast.makeText(DeleteContact.this,"You haven't selected a contact", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = getIntent();
                    intent.putExtra("contact", selected);
                    setResult(Activity.RESULT_OK, intent);
                }
            }
        });

        select_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteContact.this);
                builder.setTitle(R.string.choose);
                List<String> contact_names = new ArrayList<String>();
                Iterator<Contact> iterator = contacts.iterator();
                while (iterator.hasNext()) {
                    contact_names.add(iterator.next().getName());
                }
                final CharSequence[] cont_names = contact_names.toArray(new String[contact_names.size()]);
                builder.setItems(cont_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = contacts.get(which);
                        name.setText(selected.getName());
                        number.setText(selected.getNumber());
                        email.setText(selected.getEmail());
                        if(selected.getPhoto() != null)
                            photo.setImageBitmap(BitmapFactory.decodeFile(selected.getPhoto().toString()));
                        else
                            photo.setImageResource(R.drawable.default_photo);
                    }
                });
                builder.create().show();
            }
        });
    }


}

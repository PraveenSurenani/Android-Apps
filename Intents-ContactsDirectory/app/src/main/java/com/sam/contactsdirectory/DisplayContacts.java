package com.sam.contactsdirectory;
/**
 * HW 2
 * DisplayContacts.java
 * Samuel Painter and Praveen Surenani
 */
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DisplayContacts extends AppCompatActivity {

    private TextView name;
    private TextView number;
    private TextView email;
    private ImageView photo;
    private ImageButton first;
    private ImageButton back;
    private ImageButton forward;
    private ImageButton last;
    private Button finish;
    private List<Contact> contacts;
    private Contact current;
    private int index;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        name = (TextView) findViewById(R.id.name_value);
        number = (TextView) findViewById(R.id.number_value);
        email = (TextView) findViewById(R.id.email_value);
        photo = (ImageView) findViewById(R.id.photo);
        first = (ImageButton) findViewById(R.id.first);
        back = (ImageButton) findViewById(R.id.back);
        forward = (ImageButton) findViewById(R.id.forward);
        last = (ImageButton) findViewById(R.id.last);
        finish = (Button) findViewById(R.id.finish);

        Intent intent = getIntent();
        contacts = intent.getParcelableArrayListExtra("contacts");
        if (contacts != null && (length = contacts.size()) != 0 ) {
            current = contacts.get(0);
            index = 0;
            setInfo();
        } else {
            Toast.makeText(this, "No contacts to display", Toast.LENGTH_SHORT).show();
            finish();
        }


        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + current.getNumber()));
                    startActivity(intent);
                }
                return;
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != null) {
                    try {
                        Uri em = Uri.parse("mailto:" + current.getEmail());
                        Intent intent = new Intent(Intent.ACTION_SEND, em);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(DisplayContacts.this,"No email applications set up", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            }
        });


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                current = contacts.get(index);
                setInfo();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != 0)
                    index--;
                current = contacts.get(index);
                setInfo();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < (length - 1))
                    index++;
                current = contacts.get(index);
                setInfo();
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = length -1;
                current = contacts.get(index);
                setInfo();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setInfo() {
        name.setText(current.getName());
        number.setText(current.getNumber());
        email.setText(current.getEmail());
        if (current.getPhoto() != null)
            photo.setImageBitmap(BitmapFactory.decodeFile(current.getPhoto().toString()));
        else
            photo.setImageResource(R.drawable.default_photo);
    }

}

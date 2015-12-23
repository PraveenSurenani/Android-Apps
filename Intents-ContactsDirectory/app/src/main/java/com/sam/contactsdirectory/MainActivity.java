package com.sam.contactsdirectory;

/**
 * HW 2
 * MainActivity.java
 * Samuel Painter and Praveen Surenani
 */

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int NEW_CONTACT_CODE = 80;
    private final int EDIT_CONTACT_CODE = 81;
    private final int DELETE_CONTACT_CODE = 82;

    private ArrayList<Contact> contacts;
    private Button create_button;
    private Button edit_button;
    private Button delete_button;
    private Button display_button;
    private Button finish_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<Contact>();
        create_button = (Button) findViewById(R.id.create_button);
        edit_button = (Button) findViewById(R.id.edit_button);
        delete_button = (Button) findViewById(R.id.delete_button);
        display_button = (Button) findViewById(R.id.display_button);
        finish_button = (Button) findViewById(R.id.finish_button);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateContact.class);
                startActivityForResult(intent, NEW_CONTACT_CODE);
            }

        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditContact.class);
                intent.putParcelableArrayListExtra("contacts", contacts);
                startActivityForResult(intent, EDIT_CONTACT_CODE);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteContact.class);
                intent.putParcelableArrayListExtra("contacts", contacts);
                startActivityForResult(intent, DELETE_CONTACT_CODE);
            }
        });

        display_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayContacts.class);
                intent.putParcelableArrayListExtra("contacts", contacts);
                startActivity(intent);
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case NEW_CONTACT_CODE:
                if (resultCode == RESULT_OK) {
                    Contact newContact = data.getParcelableExtra("contact");
                    contacts.add(newContact);
                }
                break;
            case EDIT_CONTACT_CODE:
                if (resultCode == RESULT_OK) {
                    Parcelable[] obs = data.getParcelableArrayExtra("edited");

                    contacts.remove(obs[0]);
                    contacts.add((Contact)obs[1]);
                }
                break;
            case DELETE_CONTACT_CODE:
                if (resultCode == RESULT_OK) {
                    Contact toDelete = data.getParcelableExtra("contact");
                    contacts.remove(toDelete);
                }
                break;
        }
    }

}

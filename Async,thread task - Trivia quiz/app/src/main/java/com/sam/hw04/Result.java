package com.sam.hw04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
/**
 * HW04
 * Sam Painter and Praveen Suenani
 * Result.java
 */
public class Result extends AppCompatActivity {

    private TextView percent;
    private SeekBar pb;
    private TextView message;
    private Button quit;
    private Button tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        percent = (TextView) findViewById(R.id.percent);
        pb = (SeekBar) findViewById(R.id.pb);
        message = (TextView) findViewById(R.id.result_message);
        quit = (Button) findViewById(R.id.quitResults);
        tryAgain = (Button) findViewById(R.id.try_again);

        Intent intent = getIntent();
        int number = intent.getIntExtra("number_questions", 0);
        int correct = intent.getIntExtra("number_correct", 0);
        float percentage = (((float) correct) / ((float) number)) * 100;

        percent.setText(Integer.toString(Math.round(percentage)) + "%");
        pb.setProgress(Math.round(percentage));
        if (number != correct) {
            message.setText(getString(R.string.do_better));
        } else {
            message.setText(getString(R.string.all_right));
        }

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this, Trivia.class);
                startActivity(intent);
            }
        });
    }

}

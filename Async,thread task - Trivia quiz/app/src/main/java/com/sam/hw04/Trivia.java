package com.sam.hw04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
/**
 * HW04
 * Sam Painter and Praveen Suenani
 * Trivia.java
 */
public class Trivia extends AppCompatActivity {

    private TextView qNumber;
    private ImageView qImage;
    private TextView qText;
    private RadioGroup qOps;
    private Button quit;
    private Button next;

    private ArrayList<Question> questions;
    private Question question;
    private ProgressDialog pd;
    private ViewAnimator animator;

    private int correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        qNumber = (TextView) findViewById(R.id.textQNumber);
        qImage = (ImageView) findViewById(R.id.qImage);
        qText = (TextView) findViewById(R.id.qText);
        qOps = (RadioGroup) findViewById(R.id.qOps);
        quit = (Button) findViewById(R.id.quitButton);
        next = (Button) findViewById(R.id.nextButton);
        animator = (ViewAnimator) findViewById(R.id.animator);

        RequestParams getAllParams = new RequestParams("GET",getString(R.string.Get_All_Api));
        new GetQuestions().execute(getAllParams);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qOps.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Trivia.this, getString(R.string.selction_error), Toast.LENGTH_SHORT ).show();
                    return;
                }
                int selID = qOps.getCheckedRadioButtonId();
                View sel = qOps.findViewById(selID);
                int answer = qOps.indexOfChild(sel);
                RequestParams checkParams = new RequestParams("POST", getString(R.string.Check_Api));
                checkParams.addParam("gid", getString(R.string.GID));
                checkParams.addParam("qid", Integer.toString(question.getQuestionID()));
                checkParams.addParam("a", Integer.toString(answer));
                new CheckAnswer().execute(checkParams);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trivia.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void loadQuestion() {
        qOps.removeAllViews();
        qText.setText(question.getQuestion());
        qNumber.setText(Integer.toString(question.getQuestionID()));
        for(int i = 0; i < question.getOptions().length; i ++) {
            RadioButton rb = new RadioButton(Trivia.this);
            rb.setText(question.getOptions()[i]);
            qOps.addView(rb);
        }
        if (question.getImage_url().equals("")) {
            Picasso.with(Trivia.this).load(R.drawable.default_image).into(qImage);
        } else {
            animator.setDisplayedChild(1);
            Picasso.with(this).load(question.getImage_url()).placeholder(R.drawable.default_image).into(qImage, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                    animator.setDisplayedChild(0);
                }
            });
        }
    }

    private class CheckAnswer extends AsyncTask<RequestParams, Void, Integer> {
        @Override
        protected Integer doInBackground(RequestParams... params) {
            BufferedReader reader;
            try {
                URL url = new URL(params[0].baseUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params[0].getEncodedParams());
                writer.flush();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                return Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            pd.dismiss();
            if (integer == 0) {
                Toast.makeText(Trivia.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            } else if (integer == 1) {
                Toast.makeText(Trivia.this, getString(R.string.right), Toast.LENGTH_SHORT).show();
                correct++;
            } else {
                Log.e("Error", "Error checking answer");
            }
            Log.d("demo",Integer.toString(questions.indexOf(question)));
            Log.d("demo", Integer.toString(questions.size() - 1));

            if (questions.indexOf(question) == questions.size() - 1) {
                Intent intent = new Intent(Trivia.this, Result.class);
                intent.putExtra("number_questions", questions.size());
                intent.putExtra("number_correct", correct);
                startActivity(intent);
                return;
            }

            question = questions.get(questions.indexOf(question) + 1);
            loadQuestion();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Trivia.this);
            pd.setTitle(getString(R.string.checking_title));
            pd.setMessage(getString(R.string.checking_mess));
            pd.setCancelable(true);
            pd.show();
        }
    }

    private class GetQuestions extends AsyncTask<RequestParams, Void, ArrayList<Question>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Trivia.this);
            pd.setTitle(getString(R.string.loading_q_title));
            pd.setMessage(getString(R.string.loading_q_mess));
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Question> questionsarr) {
            super.onPostExecute(questions);
            questions = questionsarr;
            question = questions.get(0);
            loadQuestion();
            pd.dismiss();
        }

        @Override
        protected ArrayList<Question> doInBackground(RequestParams... params) {
            ArrayList<Question> qs = new ArrayList<Question>();
            BufferedReader reader;
            try {
                URL url = new URL(params[0].getEncodedUrl());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null){
                    qs.add(Question.QuestionUtil.genQuestion(line));
                }
                return qs;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}

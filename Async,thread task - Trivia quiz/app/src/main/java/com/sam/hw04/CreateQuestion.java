package com.sam.hw04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HW04
 * Sam Painter and Praveen Suenani
 * CreateQuestion.java
 */
public class CreateQuestion extends AppCompatActivity {

    private Button selectImage;
    private ImageView image;
    private EditText question;
    private EditText option;
    private TextView add;
    private RadioGroup options;
    private Button submit;
    private Bitmap bitmap;

    private ProgressDialog pd;

    private String questionString;
    private String[] optionList;
    private int index;
    private Boolean hasImage = false;
    private String imageString;

    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        selectImage = (Button)findViewById(R.id.buttonSelectImage);
        image = (ImageView)findViewById(R.id.imageView);
        question = (EditText)findViewById(R.id.question);
        option = (EditText)findViewById(R.id.answer);
        add = (TextView)findViewById(R.id.add);
        options = (RadioGroup)findViewById(R.id.options);
        submit = (Button)findViewById(R.id.buttonSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionString = "";
                int num_ops = options.getChildCount();
                optionList = new String[num_ops];
                String nullString = null;
                if((questionString = question.getText().toString()).equals(nullString)) {
                    Toast.makeText(CreateQuestion.this, getString(R.string.null_question_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num_ops < 2 || num_ops > 7) {
                    Toast.makeText(CreateQuestion.this,getString(R.string.options_error),Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < num_ops; i++) {
                    RadioButton cur = (RadioButton)options.getChildAt(i);
                    optionList[i] = cur.getText().toString();
                }
                int selected_id = options.getCheckedRadioButtonId();
                if(selected_id == -1) {
                    Toast.makeText(CreateQuestion.this,getString(R.string.checked_error),Toast.LENGTH_SHORT).show();
                    return;
                }
                View sel = options.findViewById(selected_id);
                index = options.indexOfChild(sel);
                postQuestion();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String op_text;
                if (options.getChildCount() > 6) {
                    Toast.makeText(CreateQuestion.this,getString(R.string.options_error),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(op_text = option.getText().toString()).equals("")) {
                    RadioButton op = new RadioButton(CreateQuestion.this);
                    op.setText(op_text);
                    options.addView(op);
                    option.setText("");
                }
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image.setImageBitmap(bitmap);
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);
                imageString = "";
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()){
                    imageString = cursor.getString(columnIndex);
                }
                cursor.close();
                hasImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void postQuestion() {
        if (hasImage) {
            new postImage().execute();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(questionString).append(";");
            for (int i = 0; i < optionList.length; i++){
                sb.append(optionList[i]).append(";");
            }
            sb.append(";");
            Log.d("d",Integer.toString(index));
            sb.append(Integer.toString(index)).append(";");

            RequestParams qparams = new RequestParams("POST",getString(R.string.Save_Api));
            qparams.addParam("gid", getString(R.string.GID));
            qparams.addParam("q", sb.toString());
            new postQuestion().execute(qparams);
        }
    }

    private class postQuestion extends AsyncTask<RequestParams,Void,Integer>{
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(CreateQuestion.this);
            pd.setTitle(getString(R.string.posting_q_title));
            pd.setMessage(getString(R.string.posting_q_mess));
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            pd.dismiss();
            if (integer == -1){
                Toast.makeText(CreateQuestion.this, getString(R.string.question_post_error), Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        @Override
        protected Integer doInBackground(RequestParams... params) {
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0].baseUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params[0].getEncodedParams());
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                Integer toreturn = -1;
                while ((line = reader.readLine()) != null) {
                    toreturn = Integer.parseInt(line);
                }
                return toreturn;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    private class postImage extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CreateQuestion.this);
            pd.setTitle(getString(R.string.posting_image_title));
            pd.setMessage(getString(R.string.posting_image_mess));
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.isEmpty() || s.equals("-1")){
                Toast.makeText(CreateQuestion.this,getString(R.string.image_upload_error),Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(questionString).append(";");
            for (int i = 0; i < optionList.length; i++){
                sb.append(optionList[i]).append(";");
            }
            sb.append(s).append(";");
            sb.append(Integer.toString(index)).append(";");

            RequestParams qparams = new RequestParams("POST",getString(R.string.Save_Api));
            qparams.addParam("gid", getString(R.string.GID));
            qparams.addParam("q", sb.toString());
            new postQuestion().execute(qparams);
        }

        @Override
        protected String doInBackground(Void... params) {
            String fileName = imageString;
            HttpURLConnection con = null;
            DataOutputStream dos = null;
            BufferedReader reader = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBuffer = 1024 * 1024 * 1;
            File sourceFile = new File(imageString);
            try {
                FileInputStream fis = new FileInputStream(sourceFile);
                URL url = new URL(getString(R.string.Upload_Api));
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoOutput(true);;
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("ENCTYPE", "multipart/form-data");
                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                con.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBuffer);
                buffer = new byte[bufferSize];
                bytesRead = fis.read(buffer,0,bufferSize);

                while (bytesRead>0) {
                    dos.write(buffer,0,bufferSize);
                    bytesAvailable = fis.available();
                    bufferSize = Math.min(bytesAvailable, maxBuffer);
                    bytesRead = fis.read(buffer,0,bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                if ((line = reader.readLine()) != null) {
                    return line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}

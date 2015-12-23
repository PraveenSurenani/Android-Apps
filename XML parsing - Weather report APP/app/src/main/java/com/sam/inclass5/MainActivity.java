package com.sam.inclass5;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
/**
 * InClass5
 * Sam Painter and Praveen Suenani
 * MainActivity.java
 */
public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Button go;
    private Switch sw;
    private View sep1;
    private View sep2;
    private TextView location;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView temp;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView precip;
    private TextView clouds;
    private ImageView image;
    private Button back;
    private Button next;
    private LinearLayout lb;
    private ArrayList<Weather> forecasts;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        go = (Button) findViewById(R.id.go);
        sw = (Switch) findViewById(R.id.method_switch);
        sep1 = (View) findViewById(R.id.sep1);
        sep2 = (View) findViewById(R.id.sep2);
        location = (TextView) findViewById(R.id.location);
        maxTemp = (TextView) findViewById(R.id.maxtemp);
        minTemp = (TextView) findViewById(R.id.mintemp);
        temp = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        pressure = (TextView) findViewById(R.id.pressure);
        wind = (TextView) findViewById(R.id.wind);
        precip = (TextView) findViewById(R.id.precip);
        clouds = (TextView) findViewById(R.id.clouds);
        image = (ImageView) findViewById(R.id.image);
        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.next);
        lb = (LinearLayout) findViewById(R.id.lowerbox);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = forecasts.size();
                Log.d("demo", Integer.toString(length));
                if (index == length -1) {
                    index = 0;
                } else {
                    index += 1;
                }
                Weather current = forecasts.get(index);
                double minTempValue = SAXWeatherParser.getMinTemp();
                double maxTempValue = SAXWeatherParser.getMaxTemp();
                String locationString = SAXWeatherParser.getLocation();
                location.setText(locationString);location.setVisibility(View.VISIBLE);
                minTemp.setText("Min Temp: " + Double.toString(minTempValue));minTemp.setVisibility(View.VISIBLE);
                maxTemp.setText("Max Temp: " + Double.toString(maxTempValue));maxTemp.setVisibility(View.VISIBLE);
                sep1.setVisibility(View.VISIBLE);
                temp.setText("Temperature: " + current.getTemperature());temp.setVisibility(View.VISIBLE);
                humidity.setText("Humidity: " + current.getHumidity());humidity.setVisibility(View.VISIBLE);
                pressure.setText("Pressure: " + current.getPressure());pressure.setVisibility(View.VISIBLE);
                wind.setText("Wind: " + current.getWindSpeed() + ", " + current.getWindDirection());wind.setVisibility(View.VISIBLE);
                precip.setText("Precipitation: " + current.getPrecipitation());precip.setVisibility(View.VISIBLE);
                clouds.setText("Clouds: " + current.getClouds());clouds.setVisibility(View.VISIBLE);
                sep2.setVisibility(View.VISIBLE);
                lb.setVisibility(View.VISIBLE);
                Picasso.with(MainActivity.this).load(getString(R.string.image_url_prelude) + current.getSymbol() + ".png").into(image);
                image.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = forecasts.size();
                if (index == 0){
                    index = length - 1;
                } else {
                    index -= 1;
                }
                Weather current = forecasts.get(index);
                double minTempValue = SAXWeatherParser.getMinTemp();
                double maxTempValue = SAXWeatherParser.getMaxTemp();
                String locationString = SAXWeatherParser.getLocation();
                location.setText(locationString);location.setVisibility(View.VISIBLE);
                minTemp.setText("Min Temp: " + Double.toString(minTempValue));minTemp.setVisibility(View.VISIBLE);
                maxTemp.setText("Max Temp: " + Double.toString(maxTempValue));maxTemp.setVisibility(View.VISIBLE);
                sep1.setVisibility(View.VISIBLE);
                temp.setText("Temperature: " + current.getTemperature());temp.setVisibility(View.VISIBLE);
                humidity.setText("Humidity: " + current.getHumidity());humidity.setVisibility(View.VISIBLE);
                pressure.setText("Pressure: " + current.getPressure());pressure.setVisibility(View.VISIBLE);
                wind.setText("Wind: " + current.getWindSpeed() + ", " + current.getWindDirection());wind.setVisibility(View.VISIBLE);
                precip.setText("Precipitation: " + current.getPrecipitation());precip.setVisibility(View.VISIBLE);
                clouds.setText("Clouds: " + current.getClouds());clouds.setVisibility(View.VISIBLE);
                sep2.setVisibility(View.VISIBLE);
                lb.setVisibility(View.VISIBLE);
                Picasso.with(MainActivity.this).load(getString(R.string.image_url_prelude) + current.getSymbol() + ".png").into(image);
                image.setVisibility(View.VISIBLE);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = input.getText().toString().trim();

                if (loc.equals("")) {
                    Toast.makeText(MainActivity.this, "Can't get weather without a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params = new RequestParams("GET",getString(R.string.API));
                params.addParam("q", loc);
                params.addParam("units", "imperial");
                params.addParam("cnt","8");
                params.addParam("mode","xml");

                if (sw.isChecked()){
                    new GetForecasteSAX().execute(params);
                } else {

                }
            }
        });

    }

    private class GetForecasteSAX extends AsyncTask<RequestParams, Void, ArrayList<Weather>> {
        @Override
        protected void onPostExecute(ArrayList<Weather> weathers) {
            super.onPostExecute(weathers);
            forecasts = weathers;
            index = 0;
            Weather current = forecasts.get(index);
            double minTempValue = SAXWeatherParser.getMinTemp();
            double maxTempValue = SAXWeatherParser.getMaxTemp();
            String locationString = SAXWeatherParser.getLocation();
            location.setText(locationString);location.setVisibility(View.VISIBLE);
            minTemp.setText("Min Temp: " + Double.toString(minTempValue));minTemp.setVisibility(View.VISIBLE);
            maxTemp.setText("Max Temp: " + Double.toString(maxTempValue));maxTemp.setVisibility(View.VISIBLE);
            sep1.setVisibility(View.VISIBLE);
            temp.setText("Temperature: " + current.getTemperature());temp.setVisibility(View.VISIBLE);
            humidity.setText("Humidity: " + current.getHumidity());humidity.setVisibility(View.VISIBLE);
            pressure.setText("Pressure: " + current.getPressure());pressure.setVisibility(View.VISIBLE);
            wind.setText("Wind: " + current.getWindSpeed() + ", " + current.getWindDirection());wind.setVisibility(View.VISIBLE);
            precip.setText("Precipitation: " + current.getPrecipitation());precip.setVisibility(View.VISIBLE);
            clouds.setText("Clouds: " + current.getClouds());clouds.setVisibility(View.VISIBLE);
            sep2.setVisibility(View.VISIBLE);
            lb.setVisibility(View.VISIBLE);
            Picasso.with(MainActivity.this).load(getString(R.string.image_url_prelude) + current.getSymbol() + ".png").into(image);
            image.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Weather> doInBackground(RequestParams... params) {
            try {
                URL url = new URL(params[0].getEncodedUrl());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    return SAXWeatherParser.parseWeather(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

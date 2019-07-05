package com.virjanand.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView cityTextView = (TextView) findViewById(R.id.cityText);
        city = cityTextView.getText().toString();

        String weatherDescription = getWeatherDescription();
        TextView weatherText = findViewById(R.id.weatherText);
        weatherText.setText(weatherDescription);

    }

    private String getWeatherDescription() {
        WeatherDownloader task = new WeatherDownloader();
        String result = "";
        try {
            result = task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + city + ",uk&appid=714b523529f97f47f15f008164ee02c7").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            String weatherInfo = jsonObject.getString("weather");
            JSONArray weatherDescriptionJSON = new JSONArray(weatherInfo);
            String weatherDescription = weatherDescriptionJSON.getJSONObject(0).getString("description");
            return weatherDescription;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public class WeatherDownloader extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL apiUrl = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}

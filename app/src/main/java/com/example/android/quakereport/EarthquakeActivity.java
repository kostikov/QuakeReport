/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private EarthQuakeAdapter mEarthQuakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mEarthQuakeAdapter = new EarthQuakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mEarthQuakeAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mEarthQuakeAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        //Создаем и запускаем AsyncTask

        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
        earthquakeAsyncTask.execute(USGS_URL);


        // Find a reference to the {@link ListView} in the layout


    }

    // Создаю класс для AsyncTask

    private URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("CreateURL", "Error to create URL");
        }

        return url;
    }

    // метод createUrl для преобразования String в URL

    private String httpMakeRequest(URL url) throws IOException {
        String jsonStringResponse = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            jsonStringResponse = readInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonStringResponse;

    }

    // Метод httpMakeRequest делает запрос на URl и получает InputStream
    // InputStream передается в метод readInputStream, который парсит InputStream в String
    // По завершению кейса try закрываются соединения httpURLConnection и inputStream

    private String readInputStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();

    }

    //Метод readInputStream принимает InputStream от метода httpMakeRequest и парсит в String

    private ArrayList<Earthquake> extractJsonResponse(String response) throws JSONException {

        ArrayList<Earthquake> output = new ArrayList<>();

        JSONObject root = new JSONObject(response);
        JSONArray featureArray = root.getJSONArray("features");

        if (featureArray.length() > 0) {
            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject properties = featureArray.getJSONObject(i);
                JSONObject currentJsonObject = properties.getJSONObject("properties");

                double mag = currentJsonObject.getDouble("mag");
                String city = currentJsonObject.getString("place");
                long timeInMilliseconds = currentJsonObject.getLong("time");
                String url = currentJsonObject.getString("url");

                output.add(new Earthquake(mag, city, timeInMilliseconds, url));
            }
        }

        return output;
    }

    //Метод extractJsonResponse нужен для создания массива объектов типа Earthquake из String

    public class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {

            if (urls.length < 0 || urls[0] == null) {
                return null;
            }

            String jsonResponse = "";
            List<Earthquake> earthquakeList = new ArrayList<>();

            try {
                jsonResponse = httpMakeRequest(createUrl(urls[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                earthquakeList = extractJsonResponse(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return earthquakeList;

        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {

            // Clear the adapter of previous earthquake data
            mEarthQuakeAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.

            if (earthquakes != null && !earthquakes.isEmpty()) {
                mEarthQuakeAdapter.addAll(earthquakes);
            }
        }
    }
}

package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

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

public class EartherquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public EartherquakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {

        String jsonResponse = "";
        List<Earthquake> earthquakeList = new ArrayList<>();

        try {
            jsonResponse = httpMakeRequest(createUrl(USGS_URL));
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

    //Метод extractJsonResponse нужен для создания массива объектов типа Earthquake из String

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

    // метод createUrl для преобразования String в URL

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


    // Метод httpMakeRequest делает запрос на URl и получает InputStream
    // InputStream передается в метод readInputStream, который парсит InputStream в String
    // По завершению кейса try закрываются соединения httpURLConnection и inputStream

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


    //Метод readInputStream принимает InputStream от метода httpMakeRequest и парсит в String

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
}

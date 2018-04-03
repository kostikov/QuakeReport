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

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private EarthQuakeAdapter mEarthQuakeAdapter;

    protected Loader earthquakeLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Создаем новый адаптер, который принимает на вход пустой массив ArrayList<Earthquake>
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

        //Инициализируем Loader
        earthquakeLoader = getLoaderManager().initLoader(0, null, this);


    }

    // Создаем новый загрузчик для URL
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        Loader<List<Earthquake>> loader = new EartherquakeLoader(this);

        return loader;
    }

    // Обновляем UI с результатом выполнения Loader
    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        mEarthQuakeAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mEarthQuakeAdapter.addAll(earthquakes);
        }

    }

    // Loader перезагружен, очищаем имеющиеся данные
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

        getLoaderManager().restartLoader(0, null, this);

    }


}

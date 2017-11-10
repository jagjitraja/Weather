package com.team.jz.weather.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherReading;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
// THIS IS THE MAIN ACTIVITY TO DISPLAY THE WEATHER DATA


    private int CURRENT_FRAGMENT = 0;
    //0 - WEATHER DETAIL FRAGMENT
    //1 - CITIES LIST FRAGMENT

    private String city;
    private double latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET WEATHER READING OBJECT FROM SPLASH INTENT

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        WeatherReading reading  = (WeatherReading) getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);

        city = reading.getCity();

        WeatherDetailFragment weatherDetailFragment = new WeatherDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.parent_layout,weatherDetailFragment);
        transaction.commit();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.details_tab:
                    //TODO: THIS IS REFRESH BUTTON TO REFRESH THE CURRENT CITY DATA
                    if(CURRENT_FRAGMENT!=0)
                        replaceFragment(new WeatherDetailFragment());

                    refreshData();
                    return true;
                case R.id.settings_tab:
                    //TODO: GO TO SETTINGS FRAGMENT
                    return true;
                case R.id.saved_cities_tab:
                    if(CURRENT_FRAGMENT!=1)

                   //TODO: GO TO CITIES LIST FRAGMENT
                    return true;
                default:
                    return false;
            }
        }
    };

    private void refreshData() {
        FetchDataTask fetchDataTask = new FetchDataTask(getApplicationContext(),this);
        fetchDataTask.execute(Utilities.FORECAST_WEATHER,city);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.parent_layout,fragment);
        transaction.commit();
    }

    @Override
    public void finishedDownloading(WeatherReading weatherReading) {

    }


    //TODO: MAKE FRESH CALL WHEN USER TAPS ON UPDATE BUTTON OR ANOTHER GESTURE

}

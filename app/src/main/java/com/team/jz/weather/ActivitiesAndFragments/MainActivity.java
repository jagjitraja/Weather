package com.team.jz.weather.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
// THIS IS THE MAIN ACTIVITY TO DISPLAY THE WEATHER DATA


    private int CURRENT_FRAGMENT;
    private String CURRENT_FRAG_KEY = "currentFrag";
    private String WEATHER_FRAG_TAG = "weatherDetailFragment";
    private String CITIES_FRAG_TAG = "citiesFragment";

    //0 - WEATHER DETAIL FRAGMENT
    //1 - CITIES LIST FRAGMENT
    private ArrayList<WeatherReading> weatherReadings;
    private ArrayList<String> cities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET WEATHER READING OBJECT FROM SPLASH INTENT
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        weatherReadings = (ArrayList<WeatherReading>) getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //transaction.commit();    transaction.add(R.id.parent_layout,weatherDetailFragment);
        //TODO: SET BOTTOM NAV ACTIVE ON CREATE WITH SAVED CITIES
        if(savedInstanceState!=null) {
            CURRENT_FRAGMENT = savedInstanceState.getInt(CURRENT_FRAG_KEY);
        }
        else{
            CURRENT_FRAGMENT = 0;
        }
        Log.d("--------------"+CURRENT_FRAGMENT, "onCreate: ");
        if(CURRENT_FRAGMENT == 0) {
            WeatherDetailFragment weatherDetailFragment;
            weatherDetailFragment = (WeatherDetailFragment) fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG);

            if (fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG) != null) {
                if (weatherDetailFragment.isAdded()) {
                    Log.d("555555555555555555555", "onCreate: ");
                    return;
                }
            } else {
                weatherDetailFragment = new WeatherDetailFragment();
            }
            weatherDetailFragment.updateWeatherReadings(weatherReadings);
            transaction.add(R.id.fragment, weatherDetailFragment, WEATHER_FRAG_TAG);
            CURRENT_FRAGMENT = 0;

        }else {
            SavedCitiesListFragment savedCitiesListFragment;
            savedCitiesListFragment = (SavedCitiesListFragment) fragmentManager.findFragmentByTag(CITIES_FRAG_TAG);
            if (fragmentManager.findFragmentByTag(CITIES_FRAG_TAG) != null) {
                if (savedCitiesListFragment.isAdded()) {
                    Log.d(";;;;;;;;;;;;;;;;;;;", "onCreate: ");
                    return;
                }
            } else {
                savedCitiesListFragment = new SavedCitiesListFragment();
            }
            transaction.add(R.id.fragment, savedCitiesListFragment, WEATHER_FRAG_TAG);
            CURRENT_FRAGMENT = 1;
        }

        transaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (item.getItemId()){
                case R.id.details_tab:
                    WeatherDetailFragment weatherDetailFragment = (WeatherDetailFragment) fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG);
                    if(weatherDetailFragment!=null) {
                        if (fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG).isAdded()) {
                            weatherDetailFragment.updateWeatherReadings(weatherReadings);
                            return true;
                        }
                    }else {
                        replaceFragment(new WeatherDetailFragment(),WEATHER_FRAG_TAG,fragmentManager);
                    }

                    weatherDetailFragment.updateWeatherReadings(weatherReadings);
                    CURRENT_FRAGMENT = 0;
                    return true;
                case R.id.settings_tab:
                    return true;

                case R.id.saved_cities_tab:
                    SavedCitiesListFragment savedCitiesListFragment = (SavedCitiesListFragment) fragmentManager.findFragmentByTag(CITIES_FRAG_TAG);
                    if(savedCitiesListFragment!=null) {
                        if (fragmentManager.findFragmentByTag(CITIES_FRAG_TAG).isAdded()) {
                            return true;
                        }
                    }else {
                        replaceFragment(new SavedCitiesListFragment(),CITIES_FRAG_TAG,fragmentManager);
                    }
                    CURRENT_FRAGMENT = 1;
                    return true;
                default:
                    return false;
            }
        }
    };

    private void refreshData() {
        FetchDataTask fetchDataTask = new FetchDataTask(getApplicationContext(),this);
        fetchDataTask.execute(Utilities.FORECAST_WEATHER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAG_KEY,CURRENT_FRAGMENT);
    }

    private void replaceFragment(Fragment fragment, String TAG, FragmentManager fragmentManager){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment,TAG);
        transaction.commit();
    }

    public void goToWeatherDataFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        WeatherDetailFragment w = (WeatherDetailFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_FRAG_TAG);
        if (w ==null){
            w = new WeatherDetailFragment();
        }

        w.updateWeatherReadings(weatherReadings);
        transaction.replace(R.id.fragment,w,WEATHER_FRAG_TAG);
        transaction.commit();
    }
    @Override
    public void finishedDownloading(ArrayList<WeatherReading> weatherReading) {
        weatherReadings = weatherReading;
    }


    //TODO: MAKE FRESH CALL WHEN USER TAPS ON UPDATE BUTTON OR ANOTHER GESTURE

}

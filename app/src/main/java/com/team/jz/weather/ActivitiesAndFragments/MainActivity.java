package com.team.jz.weather.ActivitiesAndFragments;


import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.DialogueMethods;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
// THIS IS THE MAIN ACTIVITY TO DISPLAY THE WEATHER DATA


    private final String WEATHER_ARRAY_LIST_KEY = "weatherArrayListKey";
    private int CURRENT_FRAGMENT;
    private String CURRENT_FRAG_KEY = "currentFrag";
    private String WEATHER_FRAG_TAG = "weatherDetailFragment";
    private String CITIES_FRAG_TAG = "citiesFragment";

    //0 - WEATHER DETAIL FRAGMENT
    //1 - CITIES LIST FRAGMENT
    private ArrayList<WeatherReading> weatherReadings;
    private ArrayList<String> cities;
    private BottomNavigationView bottomNavigationView;
    private  FetchDataTask fetchDataTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherReadings = (ArrayList<WeatherReading>) getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);
        if(savedInstanceState!=null){
            weatherReadings = (ArrayList<WeatherReading>) savedInstanceState.getSerializable(WEATHER_ARRAY_LIST_KEY);
        }

        fetchDataTask = new FetchDataTask(getApplicationContext(),this);
        //GET WEATHER READING OBJECT FROM SPLASH INTENT
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


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
                    weatherDetailFragment.updateWeatherReadings(weatherReadings);
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
            item.setChecked(true);

            switch (item.getItemId()){
                case R.id.details_tab:
                    WeatherDetailFragment weatherDetailFragment = (WeatherDetailFragment) fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG);
                    if(weatherDetailFragment!=null) {
                        if (fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG).isAdded()) {
                            weatherDetailFragment.updateWeatherReadings(weatherReadings);
                        }
                    }else {
                        weatherDetailFragment = new WeatherDetailFragment();
                    }
                    weatherDetailFragment.updateWeatherReadings(weatherReadings);
                    replaceFragment(weatherDetailFragment,WEATHER_FRAG_TAG,fragmentManager);
                    Log.d((weatherReadings==null)+"a", "onNavigationItemSelected: ");
                    CURRENT_FRAGMENT = 0;
                    return true;
                case R.id.settings_tab:
                    Intent setting = new Intent(getApplicationContext() ,SettingsActivity.class);
                    startActivity(setting);
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
        fetchDataTask.execute(Utilities.FORECAST_WEATHER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(WEATHER_ARRAY_LIST_KEY,weatherReadings);
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
        Menu menu = bottomNavigationView.getMenu();
        menu.getItem(0).setChecked(true);
        menu.getItem(1).setChecked(false);
        w.updateWeatherReadings(weatherReadings);
        transaction.replace(R.id.fragment,w,WEATHER_FRAG_TAG);
        transaction.commit();
    }

    @Override
    public void finishedDownloading(ArrayList<WeatherReading> weatherReading) {

        if(weatherReading == null){
            DialogueMethods d = new DialogueMethods(getApplicationContext(),fetchDataTask);
            d.showExplanationDialogue(getString(R.string.error_location),this);
            return;
        }
        Log.d("aaaaaaaaaaaaaaaaa", "finishedDownloading: ");
        weatherReadings = weatherReading;
        goToWeatherDataFragment();
    }

    //TODO: MAKE FRESH CALL WHEN USER TAPS ON UPDATE BUTTON OR ANOTHER GESTURE

}

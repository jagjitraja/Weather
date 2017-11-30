package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.DialogueMethods;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.team.jz.weather.ActivitiesAndFragments.SettingsActivity.SHARED_PREFS_KEY;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
// THIS IS THE MAIN ACTIVITY TO DISPLAY THE WEATHER DATA


    private final String WEATHER_ARRAY_LIST_KEY = "weatherArrayListKey";
    public static final String CITIES_ARRAY_LIST_KEY = "weatherArrayListKey";
    private int CURRENT_FRAGMENT;
    private String CURRENT_FRAG_KEY = "currentFrag";
    private String WEATHER_FRAG_TAG = "weatherDetailFragment";
    private String CITIES_FRAG_TAG = "citiesFragment";

    //0 - WEATHER DETAIL FRAGMENT
    //1 - CITIES LIST FRAGMENT
    private ArrayList<WeatherReading> weatherReadings;
    private ArrayList<String> cities;
    private BottomNavigationView bottomNavigationView;
    private FetchDataTask fetchDataTask;
    private WeatherDetailFragment weatherDetailFragment;
    private SavedCitiesListFragment savedCitiesListFragment;
    private String city;
    private DialogueMethods d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        d = new DialogueMethods(getApplicationContext(),fetchDataTask);
        fetchDataTask = new FetchDataTask(getApplicationContext(),this);
        weatherReadings = (ArrayList<WeatherReading>) getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);
        cities = new ArrayList<>();

        if(weatherReadings == null){
            SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS_KEY,MODE_PRIVATE);
            if(city==null) {
                city = sharedPref.getString(SplashActivity.CITY_PREF_KEY, "KAMLOOPS");
            }
            try {
                weatherReadings = fetchDataTask.execute(Utilities.FORECAST_WEATHER,city).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if(savedInstanceState!=null){
            weatherReadings = (ArrayList<WeatherReading>) savedInstanceState.getSerializable(WEATHER_ARRAY_LIST_KEY);
            cities = (ArrayList<String>) savedInstanceState.getSerializable(CITIES_ARRAY_LIST_KEY);
        }

        //GET WEATHER READING OBJECT FROM SPLASH INTENT
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        for(int i = 0;i<bottomNavigationView.getMenu().size();i++){
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        //TODO: SET BOTTOM NAV ACTIVE ON CREATE WITH SAVED CITIES
        if(savedInstanceState!=null) {
            CURRENT_FRAGMENT = savedInstanceState.getInt(CURRENT_FRAG_KEY);
        }
        else{
            CURRENT_FRAGMENT = 0;
        }

        if(CURRENT_FRAGMENT == 0) {
            weatherDetailFragment = (WeatherDetailFragment) fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG);
            if (fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG) != null) {
                if (weatherDetailFragment.isAdded()) {
                    weatherDetailFragment.updateWeatherReadings(weatherReadings);
                    return;
                }
            } else {
                weatherDetailFragment = new WeatherDetailFragment();
            }
            weatherDetailFragment.updateWeatherReadings(weatherReadings);
            transaction.add(R.id.fragment, weatherDetailFragment, WEATHER_FRAG_TAG);
            CURRENT_FRAGMENT = 0;

        }else {
            savedCitiesListFragment = (SavedCitiesListFragment) fragmentManager.findFragmentByTag(CITIES_FRAG_TAG);
            if (fragmentManager.findFragmentByTag(CITIES_FRAG_TAG) != null) {
                if (savedCitiesListFragment.isAdded()) {
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

            for(int i = 0;i<bottomNavigationView.getMenu().size();i++){
                bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }
            item.setChecked(true);

            switch (item.getItemId()){
                case R.id.details_tab:
                    refreshData();
                    weatherDetailFragment = (WeatherDetailFragment) fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG);
                    if(weatherDetailFragment!=null) {
                        if (fragmentManager.findFragmentByTag(WEATHER_FRAG_TAG).isAdded()) {
                            weatherDetailFragment.updateWeatherReadings(weatherReadings);
                        }
                    }else {
                        weatherDetailFragment = new WeatherDetailFragment();
                    }
                    weatherDetailFragment.updateWeatherReadings(weatherReadings);
                    replaceFragment(weatherDetailFragment,WEATHER_FRAG_TAG,fragmentManager);
                    CURRENT_FRAGMENT = 0;
                    return true;
                case R.id.settings_tab:
                    Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;

                case R.id.saved_cities_tab:
                    savedCitiesListFragment = (SavedCitiesListFragment) fragmentManager.findFragmentByTag(CITIES_FRAG_TAG);
                    if(savedCitiesListFragment!=null) {
                        if (fragmentManager.findFragmentByTag(CITIES_FRAG_TAG).isAdded()) {
                            return true;
                        }
                    }else {
                        savedCitiesListFragment = new SavedCitiesListFragment();
                        replaceFragment(savedCitiesListFragment,CITIES_FRAG_TAG,fragmentManager);
                    }
                    CURRENT_FRAGMENT = 1;
                    return true;
                default:
                    return false;
            }
        }
    };

    private void refreshData() {
        city = weatherReadings.get(0).getCity();
        fetchDataTask = new FetchDataTask(getApplicationContext(),this);
        fetchDataTask.execute(Utilities.FORECAST_WEATHER,city);
        weatherDetailFragment.updateWeatherReadings(weatherReadings);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(WEATHER_ARRAY_LIST_KEY,weatherReadings);
        outState.putSerializable(CITIES_ARRAY_LIST_KEY,cities);
    }

    private void replaceFragment(Fragment fragment, String TAG, FragmentManager fragmentManager){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment,TAG);
        transaction.commit();
    }

    public void goToWeatherDataFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        weatherDetailFragment = (WeatherDetailFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_FRAG_TAG);
        if (weatherDetailFragment ==null){
            weatherDetailFragment = new WeatherDetailFragment();
        }
        Menu menu = bottomNavigationView.getMenu();
        menu.getItem(0).setChecked(true);
        menu.getItem(1).setChecked(false);
        weatherDetailFragment.updateWeatherReadings(weatherReadings);
        transaction.replace(R.id.fragment,weatherDetailFragment,WEATHER_FRAG_TAG);
        transaction.commit();
    }
    public void setCurrentCity(String c){
        city = c;
    }


    public void setCities(ArrayList cities){
        this.cities = cities;
    }
    @Override
    public void finishedDownloading(ArrayList<WeatherReading> weatherReading) {

        if(weatherReading == null){
            d.showExplanationDialogue(getString(R.string.error_location),this);
            return;
        }
        weatherReadings = weatherReading;
        city = weatherReading.get(0).getCity();
        goToWeatherDataFragment();
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    //TODO: MAKE FRESH CALL WHEN USER TAPS ON UPDATE BUTTON OR ANOTHER GESTURE

}

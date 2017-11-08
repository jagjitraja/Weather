package com.team.jz.weather.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.WeatherReading;

public class MainActivity extends AppCompatActivity {
// THIS IS THE MAIN ACTIVITY TO DISPLAY THE WEATHER DATA

    private WeatherReading todayWeatherReading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET WEATHER READING OBJECT FROM SPLASH INTENT
        todayWeatherReading = (WeatherReading) getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);

        if(todayWeatherReading!=null)
            updateUIData();

     //   BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_menu);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.details_tab:
                    //TODO: THIS IS REFRESH BUTTON TO REFRESH THE CURRENT CITY DATA
                    return true;
                case R.id.settings_tab:
                    //TODO: GO TO SETTINGS ACTIVITY
                    return true;
                case R.id.saved_cities_tab:
                   //TODO: GO TO CITIES LIST ACTIVITY
                    return true;
                default:
                    return false;
            }
        }
    };


    private void updateUIData() {

        //UPDATE THE UI ELEMENTS OF THE SCREEN

        TextView city = (TextView) findViewById(R.id.city_name);
        TextView temp = (TextView) findViewById(R.id.weather_temperature);
        ImageView weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        TextView type = (TextView) findViewById(R.id.weather_type);
        TextView min_temp = (TextView) findViewById(R.id.min_temp_val);
        TextView max_temp = (TextView) findViewById(R.id.max_temp_val);
        TextView humidity = (TextView) findViewById(R.id.humidity_val);
        TextView pressure = (TextView) findViewById(R.id.pressure_val);
        TextView wind_speed = (TextView) findViewById(R.id.wind_val);

        weatherIcon.setBackgroundResource(todayWeatherReading.getWeatherIcon());
        city.setText(todayWeatherReading.getCity());
        temp.setText(todayWeatherReading.getTemp()+getResources().getString(R.string.super_script)+" C");
        type.setText(todayWeatherReading.getWeatherType().toString());
        min_temp.setText(todayWeatherReading.getMin_temperature()+getResources().getString(R.string.super_script)+" C");
        max_temp.setText(todayWeatherReading.getMax_temperature()+getResources().getString(R.string.super_script)+" C");
        humidity.setText(todayWeatherReading.getHumidity()+" %");
        pressure.setText(todayWeatherReading.getPressure()+" hpa");
        wind_speed.setText(todayWeatherReading.getWind_speed()+"m/s\n"+todayWeatherReading.getWind_direction()+"deg");

    }


    //TODO: MAKE FRESH CALL WHEN USER TAPS ON UPDATE BUTTON OR ANOTHER GESTURE

}

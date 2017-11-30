package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;

import static com.team.jz.weather.ActivitiesAndFragments.SplashActivity.CITY_PREF_KEY;

public class WeatherDetailFragment extends Fragment {

    private ArrayList<WeatherReading> weatherReadings;

    private TextView city;
    private TextView temp ;
    private ImageView weatherIcon;
    private TextView type ;
    private TextView min_temp ;
    private TextView max_temp;
    private TextView humidity;
    private TextView pressure ;
    private TextView wind_speed;
    private  ListView list;
    private SharedPreferences sharedPreferences;
    public WeatherDetailFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View weatherDetailView = inflater.inflate(R.layout.fragment_weather_detail,container,false);

        View view = weatherDetailView.findViewById(R.id.background_image);


        view.setBackgroundResource(weatherReadings.get(0).getBackground());
        view.getBackground().setAlpha(200);
        city = (TextView) weatherDetailView.findViewById(R.id.city_name);
        temp = (TextView) weatherDetailView.findViewById(R.id.weather_temperature);
        weatherIcon = (ImageView) weatherDetailView.findViewById(R.id.weather_icon);
        type = (TextView) weatherDetailView.findViewById(R.id.weather_type);
        min_temp = (TextView) weatherDetailView.findViewById(R.id.min_temp_val);
        max_temp = (TextView) weatherDetailView.findViewById(R.id.max_temp_val);
        humidity = (TextView) weatherDetailView.findViewById(R.id.humidity_val);
        pressure = (TextView) weatherDetailView.findViewById(R.id.pressure_val);
        wind_speed = (TextView) weatherDetailView.findViewById(R.id.wind_val);
        weatherIcon.setBackgroundResource(weatherReadings.get(0).getWeatherIcon());
        sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(getContext());
        getUnit();

        Button shareButton = (Button) weatherDetailView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareActionProvider shareActionProvider = new ShareActionProvider(getContext());
                Intent intent = new Intent(Intent.ACTION_SEND);
                shareActionProvider.setShareIntent(intent);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n"+ weatherReadings.get(0).getCity()+" WEATHER\n");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "The weather in "
                        + weatherReadings.get(0).getCity()
                        +" is " + weatherReadings.get(0).toString());
                startActivity(Intent.createChooser(intent,getString(R.string.share)));

            }
        });
//TODO: OPTIMIZE THESE
        city.setText(weatherReadings.get(0).getCity());
        temp.setText(weatherReadings.get(0).getTemp()+getResources().getString(R.string.super_script)+getUnit());
        type.setText(weatherReadings.get(0).getWeatherType().toString());
        min_temp.setText(weatherReadings.get(0).getMin_temperature()+getResources().getString(R.string.super_script)+getUnit());
        max_temp.setText(weatherReadings.get(0).getMax_temperature()+getResources().getString(R.string.super_script)+getUnit());
        humidity.setText(weatherReadings.get(0).getHumidity()+" %");
        pressure.setText(weatherReadings.get(0).getPressure()+" hpa");
        wind_speed.setText(weatherReadings.get(0).getWind_speed()+"m/s\n"+weatherReadings.get(0).getWind_direction()+"deg");

        list = weatherDetailView.findViewById(R.id.forecast_list);

        list.setAdapter(new WeatherReadingForecastAdapter(getContext(),R.layout.forecast_weather_reading_item,
                weatherReadings.subList(1,weatherReadings.size())));

        sharedPreferences = getContext().getSharedPreferences(SettingsActivity.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor s = sharedPreferences.edit();
        s.putString(weatherReadings.get(0).getCity(),CITY_PREF_KEY).apply();
        s.commit();
        return weatherDetailView;
    }
    private String getUnit(){
        int c = Integer.parseInt(sharedPreferences.getString("units_pref_list","0"));
        String unit = " C";
        if(c==0){
            unit = " C";
        }else{
            unit = " F";
        }
        return unit;
    }

    public void updateWeatherReadings(ArrayList<WeatherReading> weatherReadings){

        this.weatherReadings = weatherReadings;
        if(weatherIcon!=null){
            weatherIcon.setBackgroundResource(weatherReadings.get(0).getWeatherIcon());
            city.setText(weatherReadings.get(0).getCity());
            temp.setText(weatherReadings.get(0).getTemp()+getResources().getString(R.string.super_script)+" C");
            type.setText(weatherReadings.get(0).getWeatherType().toString());

            min_temp.setText(weatherReadings.get(0).getMin_temperature()+getResources().getString(R.string.super_script)+getUnit());
            max_temp.setText(weatherReadings.get(0).getMax_temperature()+getResources().getString(R.string.super_script)+getUnit());
            humidity.setText(weatherReadings.get(0).getHumidity()+" %");
            pressure.setText(weatherReadings.get(0).getPressure()+" hpa");
            wind_speed.setText(weatherReadings.get(0).getWind_speed()+"m/s\n"+weatherReadings.get(0).getWind_direction()+"deg");

            list.setAdapter(new WeatherReadingForecastAdapter(getContext(),R.layout.forecast_weather_reading_item,
                    weatherReadings.subList(1,weatherReadings.size())));
        }
    }

}


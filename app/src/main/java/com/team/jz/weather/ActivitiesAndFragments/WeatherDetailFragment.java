package com.team.jz.weather.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;

public class WeatherDetailFragment extends Fragment {

    private ArrayList<WeatherReading> weatherReadings;

    public WeatherDetailFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherReadings = (ArrayList<WeatherReading>) getActivity().getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View weatherDetailView = inflater.inflate(R.layout.fragment_weather_detail,container,false);

        TextView city = (TextView) weatherDetailView.findViewById(R.id.city_name);
        TextView temp = (TextView) weatherDetailView.findViewById(R.id.weather_temperature);
        ImageView weatherIcon = (ImageView) weatherDetailView.findViewById(R.id.weather_icon);
        TextView type = (TextView) weatherDetailView.findViewById(R.id.weather_type);
        TextView min_temp = (TextView) weatherDetailView.findViewById(R.id.min_temp_val);
        TextView max_temp = (TextView) weatherDetailView.findViewById(R.id.max_temp_val);
        TextView humidity = (TextView) weatherDetailView.findViewById(R.id.humidity_val);
        TextView pressure = (TextView) weatherDetailView.findViewById(R.id.pressure_val);
        TextView wind_speed = (TextView) weatherDetailView.findViewById(R.id.wind_val);

        weatherIcon.setBackgroundResource(weatherReadings.get(0).getWeatherIcon());
        city.setText(weatherReadings.get(0).getCity());
        temp.setText(weatherReadings.get(0).getTemp()+getResources().getString(R.string.super_script)+" C");
        type.setText(weatherReadings.get(0).getWeatherType().toString());
        min_temp.setText(weatherReadings.get(0).getMin_temperature()+getResources().getString(R.string.super_script)+" C");
        max_temp.setText(weatherReadings.get(0).getMax_temperature()+getResources().getString(R.string.super_script)+" C");
        humidity.setText(weatherReadings.get(0).getHumidity()+" %");
        pressure.setText(weatherReadings.get(0).getPressure()+" hpa");
        wind_speed.setText(weatherReadings.get(0).getWind_speed()+"m/s\n"+weatherReadings.get(0).getWind_direction()+"deg");

        Log.d(weatherReadings.get(0).getCity()+"aaaaaaaaaaaaa", "onCreateView: ");

        ListView list = weatherDetailView.findViewById(R.id.forecast_list);
        list.setAdapter(new WeatherReadingForecastAdapter(getContext(),R.layout.forecast_weather_reading_item,
                weatherReadings.subList(1,weatherReadings.size())));

        return weatherDetailView;
    }


}


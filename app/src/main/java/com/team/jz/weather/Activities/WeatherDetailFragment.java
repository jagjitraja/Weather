package com.team.jz.weather.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.WeatherReading;

public class WeatherDetailFragment extends Fragment {

    private WeatherReading todayWeatherReading;

    public WeatherDetailFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todayWeatherReading = (WeatherReading) getActivity().getIntent().getSerializableExtra(SplashActivity.WEATHER_READING_KEY);

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

        System.out.println(todayWeatherReading.toString()+"=-==========");
        weatherIcon.setBackgroundResource(todayWeatherReading.getWeatherIcon());
        city.setText(todayWeatherReading.getCity());
        temp.setText(todayWeatherReading.getTemp()+getResources().getString(R.string.super_script)+" C");
        type.setText(todayWeatherReading.getWeatherType().toString());
        min_temp.setText(todayWeatherReading.getMin_temperature()+getResources().getString(R.string.super_script)+" C");
        max_temp.setText(todayWeatherReading.getMax_temperature()+getResources().getString(R.string.super_script)+" C");
        humidity.setText(todayWeatherReading.getHumidity()+" %");
        pressure.setText(todayWeatherReading.getPressure()+" hpa");
        wind_speed.setText(todayWeatherReading.getWind_speed()+"m/s\n"+todayWeatherReading.getWind_direction()+"deg");
        return weatherDetailView;
    }

}

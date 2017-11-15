package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;

public class WeatherReadingForecastListFragment extends ListFragment {

    ArrayList<WeatherReading> weatherReadings;

    public WeatherReadingForecastListFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("qwqwqwqwqwqwqweqweqwe", "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast_weather_reading_list, container, false);
        Log.d("eeeeeeeeee", "onCreateView: ");
        ListView list = view.findViewById(R.id.forecast_list);
        list.setAdapter(new WeatherReadingForecastAdapter(getContext(),R.layout.forecast_weather_reading_item,
                weatherReadings));

        return view;
    }

}

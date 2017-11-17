package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.List;

public class WeatherReadingForecastAdapter extends ArrayAdapter<WeatherReading> {

    private  List<WeatherReading> weatherReadings;

    public WeatherReadingForecastAdapter(@NonNull Context context, @LayoutRes int resource,  @NonNull List<WeatherReading> objects) {
        super(context, resource,  objects);
        weatherReadings = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder weatherItemHolder;

        if(convertView==null){
            weatherItemHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.forecast_weather_reading_item,parent,false);
            weatherItemHolder.day = convertView.findViewById(R.id.day);
            weatherItemHolder.temp = convertView.findViewById(R.id.temp);
            weatherItemHolder.forecast_image = convertView.findViewById(R.id.forecast_image);
            convertView.setTag(weatherItemHolder);
        }else{
            weatherItemHolder = (ViewHolder) convertView.getTag();
        }

        weatherItemHolder.temp.setText(weatherReadings.get(position).getTemp() + " " + getContext().getString(R.string.super_script));
        weatherItemHolder.day.setText(weatherReadings.get(position).getDate());
        weatherItemHolder.forecast_image.setBackgroundResource(weatherReadings.get(position).getWeatherIcon());

        return convertView;
    }

    public int getItemCount() {
        return weatherReadings.size();
    }

    public class ViewHolder {
        public TextView day;
        public TextView temp;
        public ImageView forecast_image;
        public ViewHolder() {
        }
    }
}

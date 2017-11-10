package com.team.jz.weather.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by T00533766 on 10/23/2017.
 */

public class WeatherJsonParser {

    String inputString;
    public WeatherJsonParser(String jsonStream)
    {
        inputString = jsonStream;
    }

    public ArrayList<WeatherReading> parseCurrentWeather(String type) throws IOException, JSONException {

        ArrayList<WeatherReading> weatherReadings = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(inputString);
        if (type.equals(Utilities.TODAY_WEATHER)) {
            JSONArray weatherObject = jsonObject.optJSONArray(Utilities.WEATHER);
            JSONObject mainObject = jsonObject.optJSONObject(Utilities.MAIN);
            JSONObject windObject = jsonObject.optJSONObject(Utilities.WIND);
            JSONObject sysObject = jsonObject.optJSONObject(Utilities.SYS);
            JSONObject coordObject = jsonObject.optJSONObject(Utilities.COORD);

            String country = sysObject.optString(Utilities.COUNTRY);
            String city = jsonObject.optString(Utilities.CITY_NAME);
            double wind_speed = windObject.optDouble(Utilities.SPEED);
            double wind_direction = windObject.optDouble(Utilities.DEG);
            double temperature = mainObject.optDouble(Utilities.TEMP);
            double pressure = mainObject.optDouble(Utilities.PRESSURE);
            double humidity = mainObject.optDouble(Utilities.HUMIDITY);
            double min_temp = mainObject.optDouble(Utilities.TEMP_MIN);
            double max_temp = mainObject.optDouble(Utilities.TEMP_MAX);
            String weather_type = weatherObject.optJSONObject(0).optString(Utilities.WEATHER_TYPE);
            String weather_description = weatherObject.optJSONObject(0).optString(Utilities.DESCRIPTION);
            double longitude = coordObject.optDouble(Utilities.LONGITUDE_PARAMETER);
            double latitude = coordObject.optDouble(Utilities.LATITUDE_PARAMETER);
            WeatherReading.WeatherType weatherType = WeatherReading.getWeatherType(weather_type);
            WeatherReading weatherReading = new WeatherReading(temperature, min_temp, max_temp, weatherType,
                    System.currentTimeMillis(), weather_description, city, humidity, pressure, wind_speed, wind_direction, country);

            weatherReading.setLatitude(latitude);
            weatherReading.setLongitude(longitude);
            weatherReadings.add(weatherReading);
        }

        else{

            JSONArray daysWeatherList = jsonObject.optJSONArray(Utilities.LIST);

            JSONObject cityObject = jsonObject.getJSONObject(Utilities.CITY_JSON_PARAMETER);
            JSONObject coordObject = cityObject.optJSONObject(Utilities.COORD);
            double longitude = coordObject.optDouble(Utilities.LONGITUDE_PARAMETER);
            double latitude = coordObject.optDouble(Utilities.LATITUDE_PARAMETER);
            String country = coordObject.optString(Utilities.COUNTRY);
            String city = coordObject.optString(Utilities.CITY_NAME);

            for(int i = 0;i<daysWeatherList.length();i++){
                JSONObject dayObject = (JSONObject) daysWeatherList.get(i);
                JSONArray weatherObject = dayObject.optJSONArray(Utilities.WEATHER);
                JSONObject mainObject = dayObject.optJSONObject(Utilities.MAIN);
                JSONObject windObject = dayObject.optJSONObject(Utilities.WIND);
                JSONObject sysObject = dayObject.optJSONObject(Utilities.SYS);

                double wind_speed = windObject.optDouble(Utilities.SPEED);
                double wind_direction = windObject.optDouble(Utilities.DEG);
                double temperature = mainObject.optDouble(Utilities.TEMP);
                double pressure = mainObject.optDouble(Utilities.PRESSURE);
                double humidity = mainObject.optDouble(Utilities.HUMIDITY);
                double min_temp = mainObject.optDouble(Utilities.TEMP_MIN);
                double max_temp = mainObject.optDouble(Utilities.TEMP_MAX);
                String weather_type = weatherObject.optJSONObject(0).optString(Utilities.WEATHER_TYPE);
                String weather_description = weatherObject.optJSONObject(0).optString(Utilities.DESCRIPTION);
                WeatherReading.WeatherType weatherType = WeatherReading.getWeatherType(weather_type);

                WeatherReading reading = new WeatherReading(temperature, min_temp, max_temp, weatherType,
                        System.currentTimeMillis(), weather_description,
                        city, humidity, pressure, wind_speed, wind_direction, country);
                reading.setLatitude(latitude);
                reading.setLongitude(longitude);
                weatherReadings.add(reading);
            }
        }

        System.out.println(weatherReadings);
        return weatherReadings;
    }

}

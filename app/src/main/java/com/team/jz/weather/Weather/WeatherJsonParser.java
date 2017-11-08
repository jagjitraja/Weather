package com.team.jz.weather.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by T00533766 on 10/23/2017.
 */

public class WeatherJsonParser {

    String inputString;
    public WeatherJsonParser(String jsonStream)
    {
        inputString = jsonStream;
    }

    public WeatherReading parseCurrentWeather() throws IOException, JSONException {

        JSONObject jsonObject = new JSONObject(inputString);
        JSONArray weatherObject = jsonObject.optJSONArray(Utilities.WEATHER);
        JSONObject mainObject = jsonObject.optJSONObject(Utilities.MAIN);
        JSONObject windObject = jsonObject.optJSONObject(Utilities.WIND);
        JSONObject sysObject = jsonObject.optJSONObject(Utilities.SYS);

        String country = sysObject.optString(Utilities.COUNTRY);
        String city = jsonObject.optString(Utilities.CITY_NAME);
        double wind_speed = windObject.optDouble(Utilities.SPEED);
        double wind_direction  = windObject.optDouble(Utilities.DEG);
        double temperature = mainObject.optDouble(Utilities.TEMP);
        double pressure = mainObject.optDouble(Utilities.PRESSURE);
        double humidity = mainObject.optDouble(Utilities.HUMIDITY);
        double min_temp = mainObject.optDouble(Utilities.TEMP_MIN);
        double max_temp = mainObject.optDouble(Utilities.TEMP_MAX);
        String weather_type = weatherObject.optJSONObject(0).optString(Utilities.WEATHER_TYPE);
        String weather_description = weatherObject.optJSONObject(0).optString(Utilities.DESCRIPTION);

        WeatherReading.WeatherType weatherType = WeatherReading.getWeatherType(weather_type);
       return new WeatherReading(temperature,min_temp,max_temp,weatherType,
                System.currentTimeMillis(),weather_description,city,humidity,pressure,wind_speed,wind_direction,country);
    }

}

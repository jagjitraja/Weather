package com.team.jz.weather.Weather;

import com.team.jz.weather.BuildConfig;

/**
 * Created by T00533766 on 10/23/2017.
 */

public class Utilities {

    public final static String OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API;
    public final static String CURRENT_WEATHER_BASE_URL ="http://api.openweathermap.org/data/2.5/weather?";
    public final static String FORERCAST_WEATHER_BASE_URL ="http://api.openweathermap.org/data/2.5/forecast?";

    public final static String CITY_PARAMETER = "q";
    public final static String LATITUDE_PARAMETER = "lat";
    public final static String LONGITUDE_PARAMETER = "lon";
    public final static String FORMAT_PARAMETER = "mode";
    public final static String FORMAT_VALUE = "json";
    public final static String UNITS_PARAMETER = "units";
    public final static String METRIC_UNITS_VALUE = "metric";
    public final static String IMPERIAL_UNITS_VALUE = "imperial";
    public final static String API_PARAMETER = "appid";
    public final static String ZIP_CODE_PARAMETER = "zip";
    public final static String TODAY_WEATHER = "TODAY";
    public final static String FORECAST_WEATHER = "FORECAST";
    public final static String CNT_VAL = "39";


    public final static String DATE = "dt";
    public final static String COORD = "coord";
    public final static String LIST = "list";
    public final static String COUNT = "cnt";
    public final static String MAIN = "main";
    public final static String WEATHER = "weather";
    public final static String WIND = "wind";
    public final static String SYS = "sys";
    public final static String TEMP_MIN = "temp_min";
    public final static String TEMP_MAX = "temp_max";
    public final static String PRESSURE = "pressure";
    public final static String HUMIDITY = "humidity";
    public final static String TEMP = "temp";
    public final static String SPEED = "speed";
    public final static String DEG = "deg";
    public final static String COUNTRY = "country";
    public final static String CITY_NAME = "name";
    public final static String CITY_JSON_PARAMETER = "city";
    public final static String WEATHER_TYPE = "main";
    public final static String DESCRIPTION = "description";

}

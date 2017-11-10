package com.team.jz.weather.Weather;

import com.team.jz.weather.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by T00533766 on 10/12/2017.
 */

public class WeatherReading implements Serializable {



    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }


    public enum WeatherType {RAIN,SNOW,CLOUDS,CLEAR,SUNNY,THUNDERSTORM}

    private String country;
    private String city;
    private WeatherType weatherType;
    private String weatherIcon;
    private String weatherDescription;
    private long dateinMillis;
    private double lowTemp;
    private double highTemp;
    private double temp;
    private double latitude;
    private double longitude;

    private double humidity;
    private double pressure;
    private double wind_speed;
    private double wind_direction;

    private DecimalFormat decimalFormat = new DecimalFormat(".##");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");

    public WeatherReading(double temp,
                          double min_temperature,
                          double max_temperature,
                          WeatherType weather_type,
                          long dateinMillis,
                          String weatherDescription,
                          String city,
                          double humidity,
                          double pressure,
                          double wind_speed,
                          double wind_direction,
                          String country){

        this.temp = temp;
        this.lowTemp = min_temperature;
        this.highTemp = max_temperature;
        this.weatherType = weather_type;
        this.weatherDescription = weatherDescription;
        this.dateinMillis = dateinMillis;
        this.city =city;
        this.country = country;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
    }

    public WeatherReading(double temp,
                          double min_temperature,
                          double max_temperature,
                          WeatherType weather_type,
                          long dateinMillis,
                          String weatherDescription,
                          String city,
                          double humidity,
                          double pressure,
                          double wind_speed,
                          double wind_direction,
                          String country,
                          double latitude,
                          double longitude){

        this.temp = temp;
        this.lowTemp = min_temperature;
        this.highTemp = max_temperature;
        this.weatherType = weather_type;
        this.weatherDescription = weatherDescription;
        this.dateinMillis = dateinMillis;
        this.city =city;
        this.country = country;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(double wind_direction) {
        this.wind_direction = wind_direction;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public WeatherType getWeatherType(){
        return weatherType;
    }
    public String getMax_temperature() {
        return decimalFormat.format(highTemp);
    }
    public String getMin_temperature() {
        return decimalFormat.format(lowTemp);
    }
    public String getWeatherDescription() {
        return weatherDescription;
    }
    public String getWeatherIconValue() {
        return weatherIcon;
    }

    public long getDateinMillis(){
        return dateinMillis;
    }
    public String getDate() {
        String date = simpleDateFormat.format(new Date(dateinMillis*1000));
        return date;
    }

    public void setTemperature(double min, double max){
        this.lowTemp = min;
        this.highTemp = max;
    }
    public void setWeatherDescription(String description){
        this.weatherDescription = description;
    }
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
    public void setDateinMillis(long dateinMillis) {
        this.dateinMillis = dateinMillis;
    }

    public void setWeatherType(String weather){
        this.weatherType = getWeatherType(weather);
    }

    public String toString(){
        return "Weather Type: "+weatherType+"\n"+
                "Temp: "+temp+"\n"+
                "Min Temp: "+getMin_temperature()+"\n"+
                "max temp: "+getMax_temperature()+"\n"+
                "description: "+weatherDescription+"\n"+
                "city: "+city+"\n"+
                "country "+country+"\n"+
                "latitude "+latitude+"\n"+
                "longitude "+longitude+"\n";
    }

    public int getWeatherIcon(){

        switch (weatherType){
            case RAIN:
                return R.drawable.rainy;
            case SNOW:
                return R.drawable.snowy;
            case CLOUDS:
                return R.drawable.cloudy;
            case CLEAR:
                return R.drawable.clear_night;
            case SUNNY:
                return R.drawable.sunny;
            default:
                return R.drawable.sunny;
        }
    }

    public int getBackground(){
        switch (weatherType){
            case RAIN:
                return (R.drawable.rainy_weather);
            case SNOW:
                return (R.drawable.snowfall_weather);
            case CLOUDS:
                return (R.drawable.rainy_weather);
            case CLEAR:
                return (R.drawable.sunny_weather);
            case SUNNY:
                return (R.drawable.sunny_weather);
            default:
                return (R.drawable.sunny_weather);
        }
    }

    public static  WeatherType getWeatherType(String temp){
        WeatherType weather = null;
        temp = temp.toUpperCase();
        switch (WeatherType.valueOf(temp)){
            case RAIN:
                weather = WeatherType.RAIN;
                break;
            case SNOW:
                weather =  WeatherType.SNOW;
                break;
            case CLOUDS:
                weather =  WeatherType.CLOUDS;
                break;
            case CLEAR:
                weather =  WeatherType.CLEAR;
                break;
            case SUNNY:
                weather = WeatherType.SUNNY;
                break;
            case THUNDERSTORM:
                weather = WeatherType.THUNDERSTORM;
            default:
                weather =  WeatherType.CLEAR;
                break;
        }
        return weather;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
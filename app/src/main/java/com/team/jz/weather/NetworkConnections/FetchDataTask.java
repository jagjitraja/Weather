package com.team.jz.weather.NetworkConnections;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team.jz.weather.R;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherJsonParser;
import com.team.jz.weather.Weather.WeatherReading;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by T00533766 on 10/19/2017.
 */

public class FetchDataTask extends AsyncTask<String, Integer, ArrayList<WeatherReading>> {

    private final String TAG = "FETCH DATA TASK";
    private Context context;
    private DownloadCallback callback;

    public FetchDataTask(Context c,DownloadCallback downloadCallback){
        context = c;
        this.callback = downloadCallback;
    }
//TODO: CHECK NETWORK STATE BEFORE EXECUTING
    @Override
    protected ArrayList<WeatherReading> doInBackground(String... strings) {

        //strings[0] = TODAY OR FORECAST
        //strings[1] = CITY OR ZIP
        ArrayList<WeatherReading> weatherReadings = null;
        Uri todayUri = null;
        if (strings.length<4){
            todayUri = Uri.parse(Utilities.FORERCAST_WEATHER_BASE_URL).buildUpon().
                    appendQueryParameter(Utilities.CITY_PARAMETER, strings[1]).
                    appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                    appendQueryParameter(Utilities.UNITS_PARAMETER, Utilities.UNITS_VALUE).
                    appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).build();
        }
        else {
            if (strings[0].equals(Utilities.TODAY_WEATHER)) {

                if (strings.length > 2) {
                    //IF LATITUDE AND LONGITUDE ARE AVAILABLE, USE TEHM TO BUUILD THE URI
                    todayUri = Uri.parse(Utilities.CURRENT_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.LATITUDE_PARAMETER, strings[2]).
                            appendQueryParameter(Utilities.LONGITUDE_PARAMETER, strings[3]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, Utilities.UNITS_VALUE).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).build();
                } else {
                    //IF LATITUDE AND LONGITUDE ARE NOT AVAOLABLE, USE CITY NAME TO BUILD URI
                    todayUri = Uri.parse(Utilities.CURRENT_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.CITY_PARAMETER, strings[1]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, Utilities.UNITS_VALUE).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).build();
                }
            } else if (strings[0].equals(Utilities.FORECAST_WEATHER)) {

                if (strings.length > 2) {
                    //IF LATITUDE AND LONGITUDE ARE AVAILABLE, USE TEHM TO BUUILD THE URI
                    todayUri = Uri.parse(Utilities.FORERCAST_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.LATITUDE_PARAMETER, strings[2]).
                            appendQueryParameter(Utilities.LONGITUDE_PARAMETER, strings[3]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, Utilities.UNITS_VALUE).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).
                            appendQueryParameter(Utilities.COUNT, Utilities.CNT_VAL).build();
                } else {
                    //IF LATITUDE AND LONGITUDE ARE NOT AVAOLABLE, USE CITY NAME TO BUILD URI
                    todayUri = Uri.parse(Utilities.FORERCAST_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.CITY_PARAMETER, strings[1]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, Utilities.UNITS_VALUE).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).
                            appendQueryParameter(Utilities.COUNT, Utilities.CNT_VAL).build();
                }
            }
        }

            if(todayUri==null){

                return null;
            }
                try{

                    URL url = new URL(todayUri.toString());

                    Log.d(TAG, "doInBackground: "+ url.toString());
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

                    int RESPONSE_CODE = httpConnection.getResponseCode();

                    if (RESPONSE_CODE>=200 && RESPONSE_CODE<=300){
                        InputStream inputStream = httpConnection.getInputStream();

                        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(inputStream));
                        String file = "";
                        String line = "";

                        while (line!=null){
                            file+=line;
                            line=inputStreamReader.readLine();
                        }
                        //THIS PARSES THE INPUT STREAM INTO JSON AND UPDATES THE VARIABLES
                        WeatherJsonParser parser = new WeatherJsonParser(file);
                        weatherReadings = parser.parseCurrentWeather(strings[0]);

                    }
                    else if (RESPONSE_CODE>=400 && RESPONSE_CODE<=500){
                        Toast.makeText(context, R.string.returned_error,Toast.LENGTH_SHORT).show();
                        Log.d("WE RUN INTO A PROBLEM "+ RESPONSE_CODE, "doInBackground: ");

                    }else if (RESPONSE_CODE>=500){
                        Toast.makeText(context, R.string.server_error,Toast.LENGTH_SHORT).show();;
                        Log.d("SERVER ERRORS"+ RESPONSE_CODE, "doInBackground: ");;
                    }
                    else{
                        Toast.makeText(context, R.string.unkown_error,Toast.LENGTH_SHORT).show();;
                        Log.d("UNKNOWN ERRORS"+ RESPONSE_CODE, "doInBackground: ");;
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

        Log.d(weatherReadings.toString()+"   ", "doInBackground: ");
        return weatherReadings;
    }


    @Override
    protected void onPostExecute(ArrayList<WeatherReading> weatherReadings) {
        super.onPostExecute(weatherReadings);
        callback.finishedDownloading(weatherReadings);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}

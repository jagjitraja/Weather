package com.team.jz.weather.NetworkConnections;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.team.jz.weather.ActivitiesAndFragments.SettingsActivity;
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
    private int RESPONSE_CODE;

    public FetchDataTask(Context c,DownloadCallback downloadCallback){
        context = c;
        this.callback = downloadCallback;
    }

//TODO: CHECK NETWORK STATE BEFORE EXECUTING
    @Override
    protected ArrayList<WeatherReading> doInBackground(String... strings) {

        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(context);
        int c = Integer.parseInt(sharedPreferences.getString("units_pref_list","0"));
        String unitVal;
        Log.d(c+"pppppppppppppppppp", "doInBackground: ");
        if(c == 0){
            unitVal =Utilities.METRIC_UNITS_VALUE;
        }else{
            unitVal =Utilities.IMPERIAL_UNITS_VALUE;
        }
        Log.d(unitVal+"88888888888888888888", "doInBackground: ");

        sharedPreferences.edit().putString("units_pref_list",c+"").apply();
        //strings[0] = TODAY OR FORECAST
        //strings[1] = CITY OR ZIP
        ArrayList<WeatherReading> weatherReadings = null;
        Uri todayUri = null;

        if (strings.length<4){
            todayUri = Uri.parse(Utilities.FORERCAST_WEATHER_BASE_URL).buildUpon().
                    appendQueryParameter(Utilities.CITY_PARAMETER, strings[1]).
                    appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                    appendQueryParameter(Utilities.UNITS_PARAMETER, unitVal).
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
                            appendQueryParameter(Utilities.UNITS_PARAMETER, unitVal).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).build();
                } else {
                    //IF LATITUDE AND LONGITUDE ARE NOT AVAOLABLE, USE CITY NAME TO BUILD URI
                    todayUri = Uri.parse(Utilities.CURRENT_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.CITY_PARAMETER, strings[1]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, unitVal).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).build();
                }
            } else if (strings[0].equals(Utilities.FORECAST_WEATHER)) {

                if (strings.length > 2) {
                    //IF LATITUDE AND LONGITUDE ARE AVAILABLE, USE TEHM TO BUUILD THE URI
                    todayUri = Uri.parse(Utilities.FORERCAST_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.LATITUDE_PARAMETER, strings[2]).
                            appendQueryParameter(Utilities.LONGITUDE_PARAMETER, strings[3]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, unitVal).
                            appendQueryParameter(Utilities.API_PARAMETER, Utilities.OPEN_WEATHER_API_KEY).
                            appendQueryParameter(Utilities.COUNT, Utilities.CNT_VAL).build();
                } else {
                    //IF LATITUDE AND LONGITUDE ARE NOT AVAOLABLE, USE CITY NAME TO BUILD URI
                    todayUri = Uri.parse(Utilities.FORERCAST_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.CITY_PARAMETER, strings[1]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER, Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER, unitVal).
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
                    RESPONSE_CODE = httpConnection.getResponseCode();
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
                        Log.d("WE RUN INTO A PROBLEM "+ RESPONSE_CODE, "doInBackground: ");
                    }else if (RESPONSE_CODE>=500){
                        Log.d("SERVER ERRORS"+ RESPONSE_CODE, "doInBackground: ");;
                    }
                    else{
                        Log.d("UNKNOWN ERRORS"+ RESPONSE_CODE, "doInBackground: ");;
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
        return weatherReadings;
    }


    @Override
    protected void onPostExecute(ArrayList<WeatherReading> weatherReadings) {
        super.onPostExecute(weatherReadings);
        callback.finishedDownloading(weatherReadings);
//        if (RESPONSE_CODE>=400 && RESPONSE_CODE<=500){
//            Toast.makeText(context, R.string.error_location_not_found,Toast.LENGTH_SHORT).show();
//        }else if (RESPONSE_CODE>=500){
//            Toast.makeText(context, R.string.server_error,Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(context, R.string.unkown_error,Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo == null && !activeNetworkInfo.isConnected()){
            Toast.makeText(context,context.getString(R.string.internet_connection),Toast.LENGTH_SHORT).show();
        }
    }
}

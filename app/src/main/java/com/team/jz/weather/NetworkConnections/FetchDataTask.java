package com.team.jz.weather.NetworkConnections;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by T00533766 on 10/19/2017.
 */

public class FetchDataTask extends AsyncTask<String, Integer, WeatherReading> {

    private final String TAG = "FETCH DATA TASK";
    private Context context;
    private DownloadCallback callback;

    public FetchDataTask(Context c,DownloadCallback downloadCallback){
        context = c;
        this.callback = downloadCallback;
    }
//TODO: CHECK NETWORK STATE BEFORE EXECUTING
    @Override
    protected WeatherReading doInBackground(String... strings) {

        //strings[0] = TODAY OR FORECAST
        //strings[1] = CITY OR ZIP
        WeatherReading weatherReading = null;
        if (strings.length<2){
            return null;
        }

            if (strings[0].equals(Utilities.TODAY_WEATHER)){
                try {
                    Uri todayUri = Uri.parse(Utilities.CURRENT_WEATHER_BASE_URL).buildUpon().
                            appendQueryParameter(Utilities.CITY_PARAMETER,strings[1]).
                            appendQueryParameter(Utilities.FORMAT_PARAMETER,Utilities.FORMAT_VALUE).
                            appendQueryParameter(Utilities.UNITS_PARAMETER,Utilities.UNITS_VALUE).
                            appendQueryParameter(Utilities.API_PARAMETER,Utilities.OPEN_WEATHER_API_KEY).build();

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
                        weatherReading = parser.parseCurrentWeather();

                    }
                    else if (RESPONSE_CODE>=400 && RESPONSE_CODE<=500){
                        Toast.makeText(context,"WE RUN INTO A PROBLEM",Toast.LENGTH_SHORT).show();
                        Log.d("WE RUN INTO A PROBLEM "+ RESPONSE_CODE, "doInBackground: ");

                    }else if (RESPONSE_CODE>=500){
                        Toast.makeText(context,"SERVERS ARE DOWN",Toast.LENGTH_SHORT).show();;
                        Log.d("SERVER ERRORS"+ RESPONSE_CODE, "doInBackground: ");;
                    }
                    else{
                        Toast.makeText(context,"NOT SURE WHAT HAPPENED",Toast.LENGTH_SHORT).show();;
                        Log.d("UNKNOWN ERRORS"+ RESPONSE_CODE, "doInBackground: ");;
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        return weatherReading;
    }


    @Override
    protected void onPostExecute(WeatherReading weatherReading) {
        super.onPostExecute(weatherReading);
        callback.finishedDownloading(weatherReading);
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

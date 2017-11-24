package com.team.jz.weather.ActivitiesAndFragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherReading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity implements DownloadCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String DEFAULT_SEARCH_CITY = "firstSearched";
    private ArrayList<WeatherReading> fetchedWeatherReadings;
    private boolean FIRST_TIME_LAUNCH;
    private String SHARED_PREFS = "sharedPrefs";
    private String FIRST_LAUNCH_KEY = "firstLaunch";
    public static final String WEATHER_READING_KEY = "weatherReading";
    private FetchDataTask fetchDataTask;
    private SharedPreferences sharedPreferences;
    private Handler handler;

    private GoogleApiClient googleApiClient;
    private final int REQUEST_LOCATION_RESPONSE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SET CONTENT VIEW FOR THE SPLASH SCREEN
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        fetchDataTask = new FetchDataTask(getApplicationContext(), this);

        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_RESPONSE);
        //CHECK IF IT IS A FRESH LAUNCH OF THE APPLICATION OR NOT
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        FIRST_TIME_LAUNCH = sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true);
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply();

        //CONNECT TO GOOGLE API FRO ACCESSING LOCATION SERVICES
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    //THIS METHOD IS CALLED AUTOMATICALLY WHEN THE WEATHER API HAS FINISHED RETURNING THE WEATHER DETAILS
    @Override
    public void finishedDownloading(ArrayList<WeatherReading> weatherReadings) {
        fetchedWeatherReadings = weatherReadings;
        //GO TO MAIN ACTIVITY
        handler.postDelayed(goToMainActivityRunnable, 1500);
        fetchDataTask = null;
        System.gc();
    }

    //RUNNABLE TO GO TO MAIN ACTIVITY
    private Runnable goToMainActivityRunnable = new Runnable() {
        @Override
        public void run() {

            if(fetchedWeatherReadings!=null) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra(WEATHER_READING_KEY, fetchedWeatherReadings);
                startActivity(intent);
                Log.d(fetchedWeatherReadings.toString(), "run: ");
                SplashActivity.this.finish();
            }
            else{
                showExplanationDialogue("We run into an error, please retry!");
            }
        }
    };

    //DISCONNECT FROM GOOGLE API IF APP IS STOPPED
    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    //MAINLY DOES GETTING DEVICE LOCATION FROM GOOGLE API
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Connected here", "....>>>>>>><<<<<<>>>>>>>>>.......");
        getLocationData();

    }

    private void getLocationData() {

        try {
            LocationRequest request = new LocationRequest();
            request.setInterval(1000);
            //HIGH ACCURACY RETURNS LOCATIONS
            request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Log.d("SHOW EXPLANATION", "SHOW PERMISSION: ");

                } else {
                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION_RESPONSE);
                    Log.d("RequesResponse", " Location Response.......");
                }
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, SplashActivity.this);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation != null) {
                double lat = lastLocation.getLatitude();
                double lon = lastLocation.getLongitude();
                Geocoder geoCoder = new Geocoder(SplashActivity.this);
                Log.d(" " + lat + " " + lon, "onConnected: ");
                List<Address> list = geoCoder.getFromLocation(lat, lon, 10);
                if (list.size() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.error_location_not_found, Toast.LENGTH_SHORT).show();
                    showSearchDialogue();
                    return;
                } else {
                    Log.d(list.get(0).getCountryName() + "", "FETCHING DATA: ");
                    fetchDataTask.execute(Utilities.FORECAST_WEATHER, list.get(0).getLocality(), lat + "", lon + "");
                }
            }
            else{
                showExplanationDialogue(getString(R.string.no_location_found_error));
            }
        } catch (SecurityException locationError) {
            locationError.printStackTrace();
            Log.d("LOCATION SECURITY ERROR", "ERROR: ");
            showExplanationDialogue(getString(R.string.location_security_error));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR GETTING ADDRESS", "ERROR: ");
        }
    }

    //DIALOGUE RESULT TO REQUEST LOCATION PERMISSION FROM USER
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_RESPONSE:
                if (grantResults.length > 0) {
                    Log.d("GRANTED", "onRequestPermissionsResult: ");
                    if(googleApiClient.isConnected()) {
                        getLocationData();
                        Log.d("Google Connected", "Connected");
                    }else{
                        googleApiClient.connect();
                        Log.d("Google Connected", "Connected...........");
                    }
                } else {
                    Log.d("REFUSED", "onRequestPermissionsResult: ");
                    showExplanationDialogue(getString(R.string.location_denied_error));
                }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("+++++++++++++++++++", "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), R.string.error_message_obtaining_location, Toast.LENGTH_SHORT).show();
        showSearchDialogue();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("+++++++++++++++++++" + location.toString(), "LOCATION CHANGED: ");
    }

    private void showExplanationDialogue(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showSearchDialogue();
            }
        });

        builder.create().show();
    }
    //SEARCH DIALOGUE IS ONLY SHOWN WHEN LOCATION SEARCH FAILED
    private void showSearchDialogue() {

        final AlertDialog.Builder searchDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.search_city_dialog, null);
        searchDialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.city_search);
        searchDialogBuilder.setPositiveButton(R.string.search, null);
        searchDialogBuilder.setCancelable(false);

        final AlertDialog searchDialog = searchDialogBuilder.create();

        //TO HANDLE THE SEARCH BUTTON CLICK EVENT
        searchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button searchButton = searchDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String c = editText.getText().toString();
                        if (c.length() > 0) {
                            if(fetchDataTask==null){
                                fetchDataTask = new FetchDataTask(getApplicationContext(),SplashActivity.this);
                            }
                            Log.d(fetchDataTask+"  "+c, "onClick: ");
                            fetchDataTask.execute(Utilities.FORECAST_WEATHER, c);
                            //SAVE THE FIRST SEARCHED CITY IN SHARED PREFERENCES
                            sharedPreferences.edit().putString(DEFAULT_SEARCH_CITY, c).apply();
                            searchDialog.dismiss();
                        } else {
                            AlertDialog.Builder errorBuilder = new AlertDialog.Builder(SplashActivity.this);
                            errorBuilder.setTitle(R.string.search_prompt_text);
                            errorBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            errorBuilder.create().show();
                        }
                    }
                });
            }
        });
        searchDialog.show();
    }
}

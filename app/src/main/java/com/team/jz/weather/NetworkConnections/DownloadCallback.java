package com.team.jz.weather.NetworkConnections;

import com.team.jz.weather.Weather.WeatherReading;

/**
 * Created by T00533766 on 10/24/2017.
 */

public interface DownloadCallback {

    public void finishedDownloading(WeatherReading weatherReading);
}

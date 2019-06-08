package com.example.kea_bank.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kea_bank.R;

public class LatLongService extends Service {

    private final static String TAG = "LatLongService";

    private static Location CPH = new Location("Copenhagen");
    private static Location ODENSE = new Location("Odense");

    private static final String BRANCH_COPENHAGEN = "Copenhagen";
    private static final String BRANCH_ODENSE = "Odense";

    private final static double CPH_LAT = 55.668601;
    private final static double CPH_LONG = 12.509811;
    private final static double ODENSE_LAT = 55.370059;
    private final static double ODENSE_LONG = 10.373912;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationListener locationListener;
    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, getResources().getString(R.string.on_create));

        CPH.setLatitude(CPH_LAT);
        CPH.setLongitude(CPH_LONG);
        ODENSE.setLatitude(ODENSE_LAT);
        ODENSE.setLongitude(ODENSE_LONG);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged() called");
                Intent broadcastIntent = new Intent("current_location");
                double[] latlng = {location.getLatitude(), location.getLongitude()};
                broadcastIntent.putExtra("coordinates", latlng);
                sendBroadcast(broadcastIntent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onProviderDisabled() called");
                Intent redirectUser = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                redirectUser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(redirectUser);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0 , locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");

        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    public static String compareLocations(Location location){
        Log.d(TAG, "compareLocations() called");
        float distance = CPH.distanceTo(location);
        if (distance < ODENSE.distanceTo(location)){
            return BRANCH_COPENHAGEN;
        } else {
            return BRANCH_ODENSE;
        }
    }

}

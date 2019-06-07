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

public class LatLongService extends Service {

    private static Location CPH = new Location("Copenhagen");
    private static Location ODENSE = new Location("Odense");

    private static final String BRANCH_COPENHAGEN = "Copenhagen";
    private static final String BRANCH_ODENSE = "Odense";

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

        CPH.setLatitude(55.668601);
        CPH.setLongitude(12.509811);
        ODENSE.setLatitude(55.370059);
        ODENSE.setLongitude(10.373912);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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

        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    public static String compareLocations(Location location){
        float distance = CPH.distanceTo(location);
        if (distance < ODENSE.distanceTo(location)){
            return BRANCH_COPENHAGEN;
        } else {
            return BRANCH_ODENSE;
        }
    }

}

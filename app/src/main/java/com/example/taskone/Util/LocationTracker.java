package com.example.taskone.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;


public class LocationTracker implements LocationListener {

    private final LocationManager locationManager;
    private MutableLiveData<Location> userLocation = new MutableLiveData<>();

    public LocationTracker(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        this.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    public Location getLocation() {
        return this.userLocation.getValue();
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public MutableLiveData<Location> getLocationCurrent() {
        return this.userLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.userLocation.setValue(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}

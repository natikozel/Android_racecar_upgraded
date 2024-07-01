package com.example.taskone.Interfaces;

import com.google.android.gms.maps.model.LatLng;

public interface MapUpdater {

    void zoom(LatLng location);
    void addMarker(LatLng location, String title);

}

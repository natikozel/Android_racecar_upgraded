package com.example.taskone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.taskone.Util.LocationTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private final ArrayList<Marker> markers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        if (mapFragment == null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return view;

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;

        LocationTracker tracker = new LocationTracker(requireContext());
        tracker.getLocationCurrent().observe(this, location -> {
            if (location != null) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                addMarker(loc, "YOU ARE HERE");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.0f));
                tracker.getLocationCurrent().removeObservers(this);
            }
        });
    }

    public void addMarker(LatLng location, String title) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
        markers.add(marker);
    }

    public void zoomIntoMarker(LatLng newLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 13.0f));
    }

    public void clearMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }
}

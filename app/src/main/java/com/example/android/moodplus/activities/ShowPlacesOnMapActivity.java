package com.example.android.moodplus.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.android.moodplus.R;
import com.example.android.moodplus.constants.PlacesConstant;
import com.example.android.moodplus.mapmodels.Results;
import com.example.android.moodplus.mapmodels.Geometry;
import com.example.android.moodplus.mapmodels.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowPlacesOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    List<Results> result = new ArrayList<Results>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_places_on_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.show_places_map);
        mapFragment.getMapAsync(this);

        result = PlacesConstant.results;
        Toast.makeText(getApplicationContext(), String.valueOf(result.size()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < result.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            Results googlePlace = result.get(i);
            double lat = Double.parseDouble(String.valueOf(googlePlace.getGeometry().getLocation().getLat()));
            double lng = Double.parseDouble(String.valueOf(googlePlace.getGeometry().getLocation().getLng()));
            String placeName = googlePlace.getName();
            String vicinity = googlePlace.getVicinity();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // add marker to map
            googleMap.addMarker(markerOptions).showInfoWindow();;
            // move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }
}
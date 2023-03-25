package com.example.android.moodplus.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android.moodplus.R;
import com.example.android.moodplus.adapter.PlaceRecyclerViewAdapter;
import com.example.android.moodplus.constants.PlacesConstant;
import com.example.android.moodplus.mapmodels.MyPlaces;
import com.example.android.moodplus.remotes.GoogleApiService;
import com.example.android.moodplus.remotes.RetrofitBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByLocationActivity extends AppCompatActivity {

    private ImageView imageViewSearch;
    private Spinner spinner_nearby_choices;
    private RecyclerView recyclerViewPlaces;
    private LinearLayout linearLayoutShowOnMap;
    double latitude;
    double longitude;

    ProgressDialog progressDialog;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;

    double lat = 0;
    double lng = 0;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_near_by);

        spinner_nearby_choices = findViewById(R.id.spinner_nearby_choices);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        recyclerViewPlaces = findViewById(R.id.recyclerViewPlaces);
        linearLayoutShowOnMap = findViewById(R.id.linearLayoutShowOnMap);

        checkPermission();

        locationService();

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = spinner_nearby_choices.getSelectedItemPosition();
                if (position == 0) {
                    Toast.makeText(NearByLocationActivity.this, "Please select valid type", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    placeType = spinner_nearby_choices.getSelectedItem().toString();
                    getNearbyPlaces();
                }
            }
        });

        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacesConstant.results = myPlaces.getResults();
                Intent intent = new Intent(getApplicationContext(), ShowPlacesOnMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void locationService() {

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            progressDialog = new ProgressDialog(NearByLocationActivity.this);
            progressDialog.setMessage("Please wait while fetching data from GPS .......");
            progressDialog.setCancelable(false);
            progressDialog.show();


            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new NearByLocationActivity.MyLocationListener();
            if (ActivityCompat.checkSelfPermission(NearByLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearByLocationActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                progressDialog.dismiss();

                return;
            }

            progressDialog.dismiss();

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(NearByLocationActivity.this);

            mFusedLocationClient.getLastLocation().addOnSuccessListener(NearByLocationActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();

                    } else {
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            if (ActivityCompat.checkSelfPermission(NearByLocationActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(NearByLocationActivity.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                        } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "GPS off", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            longitude = loc.getLongitude();
            latitude = loc.getLatitude();

            lat = loc.getLatitude();
            lng = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private String buildUrl(double latit, double longi, String API_KEY) {
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");

        urlString.append("&location=");
        urlString.append(latit);
        urlString.append(",");
        urlString.append(longi);
        urlString.append("&radius=5000"); // places between 5 kilometer
        urlString.append("&types=").append(placeType.toLowerCase());
        urlString.append("&sensor&key=").append(API_KEY);

        return urlString.toString();
    }

    private void getNearbyPlaces() {

        if (lat != 0 && lng != 0) {

            ProgressDialog dialog = new ProgressDialog(NearByLocationActivity.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();

            String apiKey = getApplicationContext().getResources().getString(R.string.google_map_key);
            String url = buildUrl(lat, lng, apiKey);

            googleApiService = RetrofitBuilder.builder().create(GoogleApiService.class);


            Call<MyPlaces> call = googleApiService.getMyNearByPlaces(url);

            call.enqueue(new Callback<MyPlaces>() {
                @Override
                public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                    //Log.d("MyPlaces", response.body().toString());
                    myPlaces = response.body();
                    Log.d("MyPlaces", myPlaces.getResults().get(0).toString());

                    dialog.dismiss();
                    PlaceRecyclerViewAdapter adapter = new PlaceRecyclerViewAdapter(NearByLocationActivity.this, myPlaces, lat, lng);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NearByLocationActivity.this);
                    recyclerViewPlaces.setLayoutManager(layoutManager);
                    recyclerViewPlaces.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPlaces.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    linearLayoutShowOnMap.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MyPlaces> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void checkPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(NearByLocationActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
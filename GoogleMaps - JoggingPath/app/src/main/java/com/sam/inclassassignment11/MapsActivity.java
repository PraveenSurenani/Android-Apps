package com.sam.inclassassignment11;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
/*
File: MapsActivity.java
Sam Painter, Praveen Surenani
InClass11
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location cLocation;
    private boolean hasStart;
    private boolean hasEnd;
    private List<LatLng> path;
    private Polyline pline;
    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria,true);
            cLocation = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable((getApplicationContext())) != ConnectionResult.SUCCESS) {
            finish();
        }
        path = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng c = new LatLng(cLocation.getLatitude(), cLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 18));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (!hasStart && !hasEnd) {
                    mMap.addMarker(new MarkerOptions().position(latLng)).setTitle("Start");
                    hasStart = true;
                    path.add(latLng);
                } else if (!hasEnd) {
                    mMap.addMarker(new MarkerOptions().position(latLng)).setTitle("End");
                    hasEnd = true;
                    path.add(latLng);
                    draw();
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (hasStart && !hasEnd) {
                    path.add(latLng);
                    draw();
                }
            }
        });
    }

    private void draw() {
        PolylineOptions line = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng point: path) {
            line.add(point);
            builder.include(point);
        }
        pline = mMap.addPolyline(line);

        if (hasEnd)
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    private void clear() {
        path = new ArrayList<>();
        hasEnd = false;
        hasStart = false;
        mMap.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cleartrip:
                clear();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        cLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

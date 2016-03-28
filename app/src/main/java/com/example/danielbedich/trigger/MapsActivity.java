package com.example.danielbedich.trigger;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity {

    GoogleMap mMap;
    LatLng currentPos;
    LatLng destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Getting reference to the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting reference to the Google Map
        mMap = mapFragment.getMap();

        Bundle bundle = getIntent().getExtras();
        if (PackageManager.PERMISSION_GRANTED == MapsActivity.this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            mMap.setMyLocationEnabled(true);
        }
        Location myLocation = mMap.getMyLocation();
        int flag = getIntent().getIntExtra("triggerFlag", 0);
        float zoom = 13;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getIntent().getDoubleExtra("destLat", 0.0), getIntent().getDoubleExtra("destLong", 0.0)), zoom));
        switch (flag) {
            case 2:
                //add current location and destination markers
                destination = new LatLng(getIntent().getDoubleExtra("destLat", 0.0), getIntent().getDoubleExtra("destLong", 0.0));
                mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(destination.latitude, destination.longitude))
                        .radius(50)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.BLUE));
                break;

            case 3:
                //add current location marker and circle
                destination = new LatLng(getIntent().getDoubleExtra("destLat", 0.0), getIntent().getDoubleExtra("destLong", 0.0));
                mMap.addMarker(new MarkerOptions().position(destination).title("Departure Location"));
                mMap.addCircle(new CircleOptions()
                        .center(destination)
                        .radius(100)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.BLUE));
                break;
            default:
                break;
        }


    }

}
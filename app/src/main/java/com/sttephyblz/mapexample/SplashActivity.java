package com.sttephyblz.mapexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Subscribe;
import com.sttephyblz.mapexample.Util.App;
import com.sttephyblz.mapexample.events.FusedLocationEvent;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final int MULTIPLE_PERMISSIONS = 1;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }

        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onPause() {
        App.getBus().unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        App.getBus().register(this);
        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Checar los permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null){
            Log.e("LOCATION", "Location not null");
            App.getBus().post(new FusedLocationEvent("Fused Location", mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Subscribe
    public void onSuccessFusedLocation(FusedLocationEvent fusedLocationEvent){
        Log.e("onSuccess", "algo");
        Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
        intent.putExtra("latitude", fusedLocationEvent.getLat());
        intent.putExtra("longitude", fusedLocationEvent.getLng());
        startActivity(intent);
    }

    public void checkPermission(){
        int result;

        String []permissions = new String[]{
              android.Manifest.permission.ACCESS_FINE_LOCATION,
              android.Manifest.permission.ACCESS_COARSE_LOCATION
        };

        List<String> listPermissionsNeeded = new ArrayList<>();

        for(String p : permissions){
            result = ContextCompat.checkSelfPermission(SplashActivity.this, p);
            if(result != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(p);
            }
        }

        if(!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(
                            new String[listPermissionsNeeded.size()])
                            ,MULTIPLE_PERMISSIONS);
        }
    }


}

package com.example.minhthanh.mapslocation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    public  GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest requestlocation;
    Marker mCurrentLocation;
    Location mLastLocation;

    public Client client;
    EditText edit;
    long timeStamp;

    WifiManager wifimanager;
    WifiInfo wifiinfor;

    List<ScanResult> results;
    String android_id;
    LocalizationThreadHandler LocateThread;
    TrackingThreadHandler TrackThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        edit = findViewById(R.id.edit_text);
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        wifimanager = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);
        wifiinfor = wifimanager.getConnectionInfo();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {        // check version
            if (ContextCompat.checkSelfPermission(this,                        // check xem đã được cấp phép truy cập maps chưa
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleAPIClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleAPIClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleAPIClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)             // callback if client connect or disconnect
                .addOnConnectionFailedListener(this)     // return if connection fail
                .addApi(LocationServices.API)           // specify which API requested by app
                .build();                              // endpoint from GG.API
        mGoogleApiClient.connect();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onMapClick(LatLng latLng) {

                Location LocationClickOnMaps = new Location("Minh Thanh");
                LocationClickOnMaps.setLatitude(latLng.latitude);
                LocationClickOnMaps.setLongitude(latLng.longitude);
                onLocationChanged(LocationClickOnMaps);
                client.SendCoordinates(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {

        requestlocation = new LocationRequest();
        requestlocation.setInterval(1000); // khoang tgian mong muốn để cập nhật vị trí hiện tại
        requestlocation.setFastestInterval(1000); // khoảng tgian nhanh nhất để cập nhật lại vị trí
        requestlocation.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); // set độ ưu tiên cập nhật
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, requestlocation, this);  // update location

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Prompt the user once explanation has been shown

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleAPIClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrentLocation != null)
            mCurrentLocation.remove();

        //Place current location marker

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mCurrentLocation = mMap.addMarker(markerOptions);

        // move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onClick(View v) {

        Location locateUniversist = new Location("University of Science");
        locateUniversist.setLatitude(10.7626772);
        locateUniversist.setLongitude(106.6803805);
        onLocationChanged(locateUniversist);

        if( v.getId() == R.id.buttonTracking)
        {
            TrackThread = new TrackingThreadHandler(wifimanager);
            TrackThread.ThreadTracking(edit);
        }
       else
        {
            LocateThread = new LocalizationThreadHandler(wifimanager);
            LocateThread.ThreadLocalization(edit);
            wifimanager.startScan();
        }
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                timeStamp = System.currentTimeMillis();
                results = wifimanager.getScanResults();
                for (ScanResult result : results) {
                    if (result.BSSID.equals("5c:1a:6f:08:b8:24")) {  // nhận RSS từ những Access Point mình mong muốn.
                        Log.d("" + result.SSID, "" + timeStamp + "," + result.level);
                        LocateThread.Send(android_id,timeStamp,result.level);

                        try {
                            TimeUnit.SECONDS.sleep(1);
                            onLocationChanged(client.GetLocation());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        client = new Client(android_id, edit, wifiinfor.getBSSID(), wifimanager);

    }
}

/********************************************** FINISH **********************************************************/


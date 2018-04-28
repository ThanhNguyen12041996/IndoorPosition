package com.example.minhthanh.mapslocation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    public  GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest requestlocation;
    Marker mCurrentLocation;
    Location mLastLocation;
    boolean flag = false;


    List<LatLng> RoomList = new ArrayList<>();
    public Client client;
    EditText edit;
    long timeStamp, timeStampEnd;
    String[] ListDetail = {"VP Khoa DTVT","BM-VTM","PTN-ĐTu","PTH-ĐTu","PTN-Cisco","PTN-Embedded","Ceslab"
            ,"PTN-logic","Deslab","Deslab","PhongThongBao","Văn Phòng Đoàn TN","Phòng Thanh tra pháp chế và cở hữu trí tuệ"
            ,"VP Công Đoàn","VP Đảng Uỷ","QHQT","VP Tạp chí Khoa học","PTN Sinh học Phân tử","PTH Sinh học phân tử 2"
            ,"P17","PTN Phân tích trung tâm","P15","P14","PTN Chuyển hóa SH","P11B","P11","P11A"};

    WifiManager wifimanager;
    WifiInfo wifiinfor;
    List<ScanResult> results;
    String android_id;
    LocalizationThreadHandler LocateThread;
    TrackingThreadHandler TrackThread;
    int i;

    private static final LatLng connect1 = new LatLng(10.762925, 106.681929);
    private static final LatLng connect2 = new LatLng(10.762752, 106.681442);

    private static final LatLng mot  =    new LatLng (10.762970596766385,106.68191347271204);
    private static final LatLng hai  =    new LatLng (10.762835222378563,106.68151818215848);
    private static final LatLng ba   =    new LatLng (10.763012757147267, 106.68144039809704);
    private static final LatLng bon  =    new LatLng (10.762957421646165,106.68136060237883);
    private static final LatLng nam  =    new LatLng (10.762281537205652,106.68164927512407);

    private static final LatLng sau  =    new LatLng ( 10.76247751749915,106.68215855956078);
    private static final LatLng bay  =    new LatLng (10.76257402539517,106.68211698532104);
    private static final LatLng tam  =    new LatLng (10.762403407319113, 106.68168380856514);
    private static final LatLng chin =    new LatLng (10.7627317976228,106.68155942112209);
    private static final LatLng muoi =    new LatLng (10.76287573588786, 106.68194834142923);
    private static final LatLng muoimot = new LatLng (10.762970596766385,106.68191347271204);

    private static final LatLng mot1  =    new LatLng (10.762970596766385,106.68191347271204);
    private static final LatLng hai1  =    new LatLng ( 10.762956433512123,106.68187491595744);
    private static final LatLng ba1   =    new LatLng (10.762940623367028,106.68182630091906);
    private static final LatLng bon1  =    new LatLng ( 10.76292646011134,106.68178707361221);
    private static final LatLng nam1  =    new LatLng(10.762913284989196,106.68174985796215);
    private static final LatLng sau1  =    new LatLng (10.762901756756818,106.6817133128643);
    private static final LatLng bay1  =    new LatLng (10.76288891101167,106.68167844414711);
    private static final LatLng tam1  =    new LatLng(10.762875406509758,106.68163787573576);
    private static final LatLng chin1 =    new LatLng(10.762855973201079,106.68158054351807);
    private static final LatLng muoi1 =    new LatLng(10.762842798075832,106.6815423220396);

    private static final LatLng PhongThongBao =    new LatLng(10.762714669952802,106.68150644749402);
    private static final LatLng VPDoan        =    new LatLng(10.762682390879815,106.68152086436749);
    private static final LatLng PhongPhapche  =    new LatLng(10.762650770560043,106.68153427541257);
    private static final LatLng VPCongdoan    =    new LatLng(10.762618491480213,106.681546010077);
    private static final LatLng DangUy        =    new LatLng(10.76258258923428,106.6815597563982);
    private static final LatLng QHQT          =    new LatLng(10.76255096890405,106.68157249689102);
    private static final LatLng Tapchi        =    new LatLng(10.76242448754996,106.68162815272808);

    private static final LatLng CNSHPtu       =    new LatLng (10.76236058809568,106.68176863342524);
    private static final LatLng  CNSHPtu2     =    new LatLng (10.762376398271192,106.68180618435144);
    private static final LatLng P17           =    new LatLng (10.76239056155272,106.68183837085962);
    private static final LatLng  PTNPtichTtam =    new LatLng (10.762405054212197,106.68187558650972);
    private static final LatLng P15           =    new LatLng(10.762417570599368,106.68190810829402);
    private static final LatLng P14           =    new LatLng (10.76243173387896,106.68194532394408);
    private static final LatLng PtnChuyenHoaSH  =    new LatLng (10.762445897157892,106.68197885155678);
    private static final LatLng P11b          =    new LatLng(10.762460719193257,106.68201573193073);
    private static final LatLng P11           =    new LatLng(10.762476199984995,106.68205294758081);
    private static final LatLng P11A          =    new LatLng(10.762491680775918,106.68208982795477);


//  double[] Lattitude = {
//
//          10.76292547197721,
//          10.762922507574755,
//          10.76292119006256,        /* VP Khoa*/
//          10.76291888441617,
//          10.762916249391715,
//          10.762913284989196,
//          10.762912626233069,
//
//          10.762910320586627,
//          10.762909661830498,
//          10.762907026805955,       /* BM VIỄN THÔNG*/
//          10.762905050537542,
//          10.762902415512974,
//          10.762901098000677,
//          10.762898133597979,
//
//          10.762896816085668,
//          10.762895498573346,
//
//          10.76289352230487,
//          10.762892204792536,       /* BỘ MÔN ĐIỆN TỬ*/
//          10.76289022852402,
//          10.7628879228774,
//          10.76288594660886,
//          10.762883970340306,
//         // 10.762880347181264,
//
//          10.762880347181264,
//          10.762878700290775,
//          10.76287672402217,        /* PTN ĐIỆN TỬ*/
//          10.762874418375462,
//          10.762871783350613,
//          10.762869477703855,
//          10.762866842678969,
//
//
//          10.762866842678969,
//          10.76286552516652,
//          10.762862231385363,       /* PTN CISCO*/
//          10.76286025511667,
//          10.762857290713567,
//          10.762856302579207,
//          10.762854326310462,
//
//         // 10.762854326310462,
//          10.76285267941982,
//          10.762850373772924,       /* PTN NHÚNG*/
//          10.762846750613475,//10.762849056260398,
//          10.762846421235333,
//          10.762844115588384,
//          10.762842798075832,
//
//         // 10.762842798075832,
//          10.762838845538154,       /*CESLAB*/
//          10.762837198647434,
//          10.762835881134844,
//          10.76283357548782,
//          10.762831269840769,
//          10.762828964193705,
//
//          10.76282731730295,
//          10.762824682277682,
//          10.762824352899527,   /* PTN LOGIC*/
//          10.762821059117925,
//          10.76281908284894,
//          10.762816777201788,
//          10.762814142176442,
//
//          10.76281183652925,
//          10.762809530882036,
//
//          10.762807554612973,
//          10.762805578343912,
//          10.762803272696656,       /* DESLAB 1*/
//          10.762801296427556,
//          10.762798661402071,
//          10.762796026376561,
//          10.762795367620189,
//
//          10.762794050107422,
//          10.76279108570368,
//          10.762789768190903,       /* DESLAB 2*/
//          10.76278746254352,
//          10.762785156896125,
//          10.76278318062691,
//          10.762780545601274,
//  };
//
//    double[] Longtitude = {
//            106.6819292,
//            106.6819219,             /* VP Khoa*/
//            106.6819151,
//            106.6819101,
//            106.6819031,
//            106.6818980500102,
//            106.6818924,
//
//            106.681889,
//            106.681884,         /* BỘ MÔN VT MẠNG*/
//            106.6818779,
//            106.6818736,
//            106.6818655,
//            106.6818605,
//            106.6818545,
//
//            106.6818508,
//            106.6818461,
//
//            106.6818414,
//            106.6818354,
//            106.681829,         /* BỘ MÔN ĐIỆN TỬ*/
//            106.6818236,
//            106.6818179,
//            106.6818119,
//            106.6818032,
//
//            106.6818032,
//            106.6817971,
//            106.6817911,        /* PTN ĐIỆN TỬ */
//            106.6817841,
//            106.6817774,
//            106.6817717,
//            106.6817653,
//
//            106.6817653,
//            106.6817603,
//            106.6817529,        /* PTN CISCO*/
//            106.68174717575312,
//            106.6817405,
//            106.6817351,
//            106.6817284,
//
//            106.6817284,
//            106.6817234,
//            106.681718,         /* PTN NHÚNG*/
//            106.68170996010302,
//            106.6817056,
//            106.6816982,
//            106.6816942,
//
//            106.6816942,
//            106.6816885,
//            106.6816818,        /* CESLAB*/
//            106.6816761,
//            106.6816701,
//            106.681664,
//            106.6816583,
//
//            106.6816536,
//            106.6816476,
//            106.6816432,        /* PTN LOGIC*/
//            106.6816362,
//            106.6816295,
//            106.6816235,
//            106.6816184,
//
//            106.6816117,
//            106.6816054,
//
//            106.6815966,
//            106.6815899,
//            106.6815849,        /* DESLAB 1*/
//            106.6815782,
//            106.6815735,
//            106.6815681,
//            106.68156243860722,
//
//            106.6815591,
//            106.6815534,
//            106.6815463,        /* DESLAB 2*/
//            106.6815409809351,
//            106.6815349,
//            106.6815289,
//            106.6815219,
//
//    };

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        edit = findViewById(R.id.edit_text);
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        wifimanager = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);
        wifiinfor = wifimanager.getConnectionInfo();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RoomList.add(mot1);
        RoomList.add(hai1);
        RoomList.add(ba1);
        RoomList.add(bon1);
        RoomList.add(nam1);
        RoomList.add(sau1);
        RoomList.add(bay1);
        RoomList.add(tam1);
        RoomList.add(chin1);
        RoomList.add(muoi1);

        RoomList.add(PhongThongBao);
        RoomList.add(VPDoan);
        RoomList.add(PhongPhapche);
        RoomList.add(VPCongdoan);
        RoomList.add(DangUy);
        RoomList.add(QHQT);
        RoomList.add(Tapchi);

        RoomList.add(CNSHPtu);
        RoomList.add(CNSHPtu2);
        RoomList.add(P17);
        RoomList.add(PTNPtichTtam);
        RoomList.add(P15);
        RoomList.add(P14);
        RoomList.add(PtnChuyenHoaSH);
        RoomList.add(P11b);
        RoomList.add(P11);
        RoomList.add(P11A);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

        PolylineOptions option = new PolylineOptions().add(mot).add(hai).add(ba).add(bon).add(nam).add(sau).add(bay).add(tam)
                .add(chin).add(muoi).add(muoimot).width(5).color(Color.BLUE).geodesic(true);
        mMap.addPolyline(option);

        PolylineOptions option2 = new PolylineOptions().add(connect1).add(connect2).width(5).color(Color.BLUE).geodesic(true);
        mMap.addPolyline(option2);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mot));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        for(i = 0; i <= 9;i++)
            createMaker(RoomList.get(i), ListDetail[i]);

        for( i = 10; i <= 16;i++)
            createMakerF(RoomList.get(i),ListDetail[i]);

        for( i = 17; i <RoomList.size();i++)
            createMakerB(RoomList.get(i),ListDetail[i]);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onMapClick(LatLng latLng) {

                Location LocationClickOnMaps = new Location("Minh Thanh");
                LocationClickOnMaps.setLatitude(latLng.latitude);
                LocationClickOnMaps.setLongitude(latLng.longitude);
                onLocationChanged(LocationClickOnMaps);
                Log.d("SHOWRA Toado: ",latLng.latitude+","+latLng.longitude);
              //  client.SendCoordinates(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
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

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
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

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mCurrentLocation = mMap.addMarker(markerOptions);
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .fillColor(Color.BLUE)
                .strokeColor(Color.WHITE)
                .radius(0.05)
                .strokeWidth(1 / 8));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void Changed(double Lat, double Long) {

        LatLng latLng = new LatLng(Lat,Long);
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .fillColor(Color.BLUE)
                .strokeColor(Color.WHITE)
                .radius(0.3)
                .strokeWidth(1 / 4));
    }

    protected Marker createMaker(LatLng Latlng, String title)
    {
         return mMap.addMarker(new MarkerOptions().position(Latlng).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.d2)));
    }

    protected Marker createMakerF(LatLng Latlng, String title)
    {
        return mMap.addMarker(new MarkerOptions().position(Latlng).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hinh3offical)));
    }

    protected Marker createMakerB(LatLng Latlng, String title)
    {
        return mMap.addMarker(new MarkerOptions().position(Latlng).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chodayb)));
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
            wifimanager.startScan();
            timeStamp = System.currentTimeMillis();
            flag = true;
        }
       else
        {
            LocateThread = new LocalizationThreadHandler(wifimanager);
            LocateThread.ThreadLocalization(edit);
            wifimanager.startScan();
            timeStamp = System.currentTimeMillis();
        }

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                results = wifimanager.getScanResults();
                timeStampEnd = System.currentTimeMillis();
                for (ScanResult result : results) {
                    if (result.SSID.equals("Minh Thanh")) {
                        if( flag == true){
                            TrackThread.Send(android_id,timeStamp,timeStampEnd,result.BSSID,result.level);
                            wifimanager.startScan();
                            timeStamp = System.currentTimeMillis();
                        }
                        else LocateThread.Send(android_id,timeStamp,timeStampEnd,result.BSSID,result.level);

//                        try {
//                            TimeUnit.SECONDS.sleep(1);
//                            onLocationChanged(client.GetLocation());
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }

        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        client = new Client(android_id, edit, wifiinfor.getBSSID());
    }
}

/********************************************** FINISH **********************************************************/


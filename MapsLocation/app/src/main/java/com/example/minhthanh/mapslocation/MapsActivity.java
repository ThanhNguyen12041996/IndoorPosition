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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener,IShowDirection {

    public GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest requestlocation;
    Marker mCurrentLocation;
    Location mLastLocation;
    double source0,sources1,des0,des1,distance;
    Circle mapCircle;
    DatabaseReference databaseReference;
    String[] LocationStr;
    LatLng latLng;
    String DataArray = "";
    Integer[] RSSI =new Integer[32];
    String source,destination;
    private Polyline mypoline;
    TimeDistance timeDistance = new TimeDistance();
    float DistanceuptoSpeed;
    ListView lv;

    String[] APsList = {"ac:86:74:49:c0:cb", "ac:86:74:49:c0:eb", "f0:9f:c2:a5:7b:58",
            "ac:86:74:49:c0:f3", "ac:86:74:49:ad:6b", "ac:86:74:49:c0:e3", "ac:86:74:49:c1:03", "f0:9f:c2:d2:dc:48",
            "78:8a:20:25:b9:ee", "ac:86:74:08:ac:e3", "ac:86:74:49:c1:13", "f0:9f:c2:d1:dc:48",
            "ac:86:74:4a:28:db", "78:8a:20:24:b9:ee", "ac:86:74:4a:27:73", "ac:86:74:49:c0:83", "ac:86:74:49:ae:0b",
            "f0:9f:c2:a4:7b:58", "ac:86:74:42:40:d3", "24:a4:3c:0f:4c:73", "ac:86:74:49:c1:33", "02:9f:c2:d1:dd:53",
            "ac:86:74:49:c0:d3", "f0:9f:c2:d1:dc:54", "ac:86:74:4a:28:cb", "ac:86:74:4a:27:63", "f0:9f:c2:d2:dc:54",
            "24:a4:3c:0f:4c:14", "ac:86:74:4a:28:bb", "f0:9f:c2:a4:7b:cb", "24:a4:3c:0f:4c:1e", "f0:9f:c2:d2:04:12"};

    List<LatLng> RoomList = new ArrayList<>();
    public static String[] ListDetail = {"Văn Phòng Khoa ĐTVT", "Bộ Môn Mạng VT", "PTN Điện tử", "PTH Điện tử", "PTN Cisco", "Bộ Môn MT-HTN", "PTN Ceslab",
            "PTN Logic Khả Trình", "PTN DESLAB","PTN DAQ","Phòng Thông báo","VP Đoàn Thanh niên","Phòng Thanh tra Pháp chế Sở hữu trí tuệ",
            "VP Công Đoàn","VP Đảng ủy","Phòng Quan hệ Quốc tế","VP Tạp chí Khoa học",
            "PTN Công nghệ sinh học Phân tử","PTN Công nghệ sinh học Phân tử 2","Phòng 17","PTN Phân tích trung tâm","Phòng 15","Phòng 14","PTN Chuyển hóa Sinh học",
            "Phòng 11B","Phòng 11","Phòng 11A"};

    WifiManager wifimanager;
    WifiInfo wifiinfor;
    List<ScanResult> results;
    String android_id;
    PolylineOptions option;
    int i;
    public IShowDistanceDuration IshowDistance;
    FragmentTransaction fragmentTransaction;
    DirectionActivity ft;
    private  LatLng position1,position2;
    SearchLocationBar fs;

    private static final LatLng linee1 = new LatLng(10.762781204357683,106.68152790516613);
    private static final LatLng linee2 = new LatLng(10.762752219074198,106.6815198585391);
    private static final LatLng lineb1 = new LatLng(10.762396819746673,106.68166067451239);
    private static final LatLng lineb2 = new LatLng(10.76238068019359,106.68169386684895);

    private static final LatLng mot = new LatLng(10.762970596766385, 106.68191347271204);
    private static final LatLng hai = new LatLng(10.762835222378563, 106.68151818215848);
    private static final LatLng ba = new LatLng(10.763012757147267, 106.68144039809704);
    private static final LatLng bon = new LatLng(10.762957421646165, 106.68136060237883);
    private static final LatLng nam = new LatLng(10.762281537205652, 106.68164927512407);

    private static final LatLng sau = new LatLng(10.76247751749915, 106.68215855956078);
    private static final LatLng bay = new LatLng(10.76257402539517, 106.68211698532104);
    private static final LatLng tam = new LatLng(10.762403407319113, 106.68168380856514);
    private static final LatLng chin = new LatLng(10.7627317976228, 106.68155942112209);
    private static final LatLng muoi = new LatLng(10.76287573588786, 106.68194834142923);
    private static final LatLng muoimot = new LatLng(10.762970596766385, 106.68191347271204);

    private static final LatLng mot1 = new LatLng(10.762970596766385, 106.68191347271204);
    private static final LatLng hai1 = new LatLng(10.762956433512123, 106.68187491595744);
    private static final LatLng ba1 = new LatLng(10.762940623367028, 106.68182630091906);
    private static final LatLng bon1 = new LatLng(10.76292646011134, 106.68178707361221);
    private static final LatLng nam1 = new LatLng(10.762913284989196, 106.68174985796215);
    private static final LatLng sau1 = new LatLng(10.762901756756818, 106.6817133128643);
    private static final LatLng bay1 = new LatLng(10.76288891101167, 106.68167844414711);
    private static final LatLng tam1 = new LatLng(10.762875406509758, 106.68163787573576);
    private static final LatLng chin1 = new LatLng(10.762855973201079, 106.68158054351807);
    private static final LatLng muoi1 = new LatLng(10.762842798075832, 106.6815423220396);

    private static final LatLng PhongThongBao = new LatLng(10.762714669952802, 106.68150644749402);
    private static final LatLng VPDoan = new LatLng(10.762682390879815, 106.68152086436749);
    private static final LatLng PhongPhapche = new LatLng(10.762650770560043, 106.68153427541257);
    private static final LatLng VPCongdoan = new LatLng(10.762618491480213, 106.681546010077);
    private static final LatLng DangUy = new LatLng(10.76258258923428, 106.6815597563982);
    private static final LatLng QHQT = new LatLng(10.76255096890405, 106.68157249689102);
    private static final LatLng Tapchi = new LatLng(10.76242448754996, 106.68162815272808);

    private static final LatLng CNSHPtu = new LatLng(10.76236058809568, 106.68176863342524);
    private static final LatLng CNSHPtu2 = new LatLng(10.762376398271192, 106.68180618435144);
    private static final LatLng P17 = new LatLng(10.76239056155272, 106.68183837085962);
    private static final LatLng PTNPtichTtam = new LatLng(10.762405054212197, 106.68187558650972);
    private static final LatLng P15 = new LatLng(10.762417570599368, 106.68190810829402);
    private static final LatLng P14 = new LatLng(10.76243173387896, 106.68194532394408);
    private static final LatLng PtnChuyenHoaSH = new LatLng(10.762445897157892, 106.68197885155678);
    private static final LatLng P11b = new LatLng(10.762460719193257, 106.68201573193073);
    private static final LatLng P11 = new LatLng(10.762476199984995, 106.68205294758081);
    private static final LatLng P11A = new LatLng(10.762491680775918, 106.68208982795477);


    @Override
    public void SendDirection(final String source1, final String destination1) {
        this.source = source1;
        this.destination = destination1;

        databaseReference.child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                if (mCurrentLocation != null)
                    mCurrentLocation.remove();
                if( mypoline != null)
                    mypoline.remove();

                source0 = (double) snap.child(source).child("latln").getValue();
                sources1 = (double) snap.child(source).child("longln").getValue();
                des0 = (double) snap.child(destination1).child("latln").getValue();
                des1 = (double) snap.child(destination1).child("longln").getValue();

                position1 = new LatLng(source0,sources1);
                position2 = new LatLng(des0,des1);
                ChangeLocationDirection(position2);

                if((source0 > 10.76277 && des0 > 10.76277) || ((source0 > 10.76254 && source0 < 10.762722) && (des0 > 10.76254 && des0 < 10.762722)) || (source0 < 10.76251 && des0 < 10.76251)){
                     option = new PolylineOptions().add(position1,position2).width(15).color(Color.argb(229,52,119,236)).geodesic(true);
                   mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1,position2);
                }
                else if( (source0 > 10.76277 && (des0 > 10.76254 && des0 < 10.762722))){
                    option = new PolylineOptions().add(position1,linee1,linee2,position2).width(15).color(Color.argb(229,52,119,236)).geodesic(true);
                    mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1,linee1)+timeDistance.CalculateDistance(position2,linee2) ;

                }
                else if(((source0 > 10.76254 && source0 < 10.762722) && des0 > 10.76277)){
                    option = new PolylineOptions().add(position1,linee2,linee1,position2).width(15).color(Color.argb(229,52,119,236)).geodesic(true);
                    mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1,linee2)+timeDistance.CalculateDistance(position2,linee1) ;

                }
                else if( (source0 > 10.76277 && (des0 < 10.76251 )))
                {
                    option = new PolylineOptions().add(position1,linee1,linee2,lineb1,lineb2,position2).width(15).color(Color.argb(229,52,119,236)).geodesic(true);
                    mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1,linee1)+timeDistance.CalculateDistance(linee2,lineb1)+ timeDistance.CalculateDistance(lineb2,position2);
                }
                else if( ((des0 > 10.76277) && source0 < 10.76251))
                {
                    option = new PolylineOptions().add(position1,lineb2,lineb1,linee2,linee1,position2).width(15).color(Color.argb(229,52,119,236)).geodesic(true);
                    mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1,lineb2)+timeDistance.CalculateDistance(linee2,lineb1)+ timeDistance.CalculateDistance(linee1,position2);

                }
                else if( ((source0 > 10.76254 && source0 < 10.762722) && des0 < 10.76251))
                {
                    option = new PolylineOptions().add(position1,lineb1,lineb2,position2).width(15).color(Color.argb(229,52,119,236)).geodesic(true);
                    mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1,lineb1)+timeDistance.CalculateDistance(position2,lineb2) ;

                }
                else if((((des0 > 10.76254 && des0 < 10.762722) && source0 < 10.76251))) {
                    option = new PolylineOptions().add(position1, lineb2, lineb1, position2).width(15).color(Color.argb(229, 52, 119, 236)).geodesic(true);
                    mypoline = mMap.addPolyline(option);
                    distance = timeDistance.CalculateDistance(position1, lineb2) + timeDistance.CalculateDistance(position2, lineb1);
                }
                DistanceuptoSpeed = (float) (distance/10);
                IshowDistance.SendData(distance,DistanceuptoSpeed);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void SendRemoveFragment() {
        if(ft.isVisible()){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ft);
        fragmentTransaction.hide(ft);
        fragmentTransaction.commit();}
        else if(fs.isVisible())
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_search, fs);
            fragmentTransaction.hide(fs);
            fragmentTransaction.commit();
        }
    }
    @Override
    public void SendLocationToDetermine(final String nodeLocation) {

        databaseReference.child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot snap) {

                if (mCurrentLocation != null)
                    mCurrentLocation.remove();
                if (mypoline != null)
                    mypoline.remove();
                ChangeLocationDirection(new LatLng((double) snap.child(nodeLocation).child("latln").getValue(),(double) snap.child(nodeLocation).child("longln").getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try{
            IshowDistance = (IShowDistanceDuration) fragment;
        }catch (ClassCastException e){
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ft = new DirectionActivity();
        fs = new SearchLocationBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
       // MenuItem searchitem = menu.findItem(R.id.action);
      // return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, ft);
            fragmentTransaction.show(ft);
            fragmentTransaction.commit();
            return true;
        }
        else if( item.getItemId() == R.id.cancelDicrection)
        {
            if( mypoline != null)
                mypoline.remove();
            if (mCurrentLocation != null)
                mCurrentLocation.remove();
            return true;
        }
        else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_search, fs);
            fragmentTransaction.show(fs);
            fragmentTransaction.commit();
            return true;
            //searchView.setVisibility(View.VISIBLE);
        }

       // return super.onOptionsItemSelected(item);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mot));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));


        for (i = 0; i <= 9; i++)
            createMaker(RoomList.get(i), ListDetail[i]);

        for (i = 10; i <= 16; i++)
            createMakerF(RoomList.get(i), ListDetail[i]);

        for (i = 17; i < RoomList.size(); i++)
            createMakerB(RoomList.get(i), ListDetail[i]);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onMapClick(LatLng latLng) {

                Location LocationClickOnMaps = new Location("Minh Thanh");
                LocationClickOnMaps.setLatitude(latLng.latitude);
                LocationClickOnMaps.setLongitude(latLng.longitude);
                onLocationChanged(LocationClickOnMaps);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("Location").child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                        LocationStr = dataSnapshot.getValue().toString().split(";");
                        ChangedTest(Double.parseDouble(LocationStr[0]),Double.parseDouble(LocationStr[1]));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
        markerOptions.title("Current Position");
        mCurrentLocation = mMap.addMarker(markerOptions);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void ChangedTest(double Lat, double Long) {

        if( mapCircle!= null)
            mapCircle.remove();
        latLng = new LatLng(Lat, Long);
        mapCircle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .fillColor(Color.RED)
                .radius(0.3)
                .strokeWidth(1 / 4));
    }

    private void ChangeLocationDirection(LatLng posis)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(posis);
        markerOptions.title("Current Position");
        mCurrentLocation = mMap.addMarker(markerOptions);
    }

    protected Marker createMaker(LatLng Latlng, String title) {
        return mMap.addMarker(new MarkerOptions().position(Latlng).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.d2)));
    }

    protected Marker createMakerF(LatLng Latlng, String title) {
        return mMap.addMarker(new MarkerOptions().position(Latlng).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hinh3offical)));
    }

    protected Marker createMakerB(LatLng Latlng, String title) {
        return mMap.addMarker(new MarkerOptions().position(Latlng).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chodayb)));
    }

    public void addDatatoFirebase(String stringdata) {
        databaseReference.child("Data").child("RSS").setValue(stringdata);
    }

    @Override
    public void onClick(View v) {

        wifimanager.startScan();
                registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                results = wifimanager.getScanResults();
                for (ScanResult result : results) {
                    if (result.SSID.equals("HCMUS Public")) {
                            for (i = 0; i < APsList.length; i++) {
                                if (result.BSSID.equals(APsList[i]))
                                    RSSI[i] = result.level;
                            }
                    }
                }
                for (i = 0; i < RSSI.length; i++ ) {
                    if ( RSSI[i] == null )
                        RSSI[i] = -110;
                    DataArray = DataArray+RSSI[i]+" ";
                }
                addDatatoFirebase(DataArray);
                wifimanager.startScan();
                DataArray = "";
                for(i = 0; i < RSSI.length;i++)
                    RSSI[i] = null;
            }

        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
}
/********************************************** FINISH **********************************************************/


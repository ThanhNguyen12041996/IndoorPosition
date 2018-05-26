package com.example.minhthanh.mapslocation;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by minh thanh on 5/26/2018.
 */

public class TimeDistance {

    int R = 6371;
    double DLat,DLong,b,c;

    public double CalculateDistance(LatLng position1,LatLng position2){

        DLat = DegreetoRadian(position1.latitude - position2.latitude);
        DLong = DegreetoRadian(position1.longitude - position2.longitude);
        b = Math.pow(Math.sin(DLat/2),2) + Math.cos(DegreetoRadian(position1.latitude))*Math.cos(DegreetoRadian(position2.latitude))
                *Math.pow(Math.sin(DLong/2),2);
        c = 2*Math.atan2(Math.sqrt(b),Math.sqrt(1-b));
        return R*c*1000;
    }

    public double DegreetoRadian(double a)
    {
        return a*(Math.PI/180);
    }

}

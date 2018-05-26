package com.example.minhthanh.mapslocation;

/**
 * Created by minh thanh on 1/22/2018.
 */

public class RSSHandler  {

    public RSSHandler()
    {
    }
    public double calculateDistance(int signLevelInDb)
    {
        double exp = ( 7.148996586 + Math.abs( signLevelInDb) - 40.0459702) / 50;
        return Math.pow(10.0,exp);
    }
}
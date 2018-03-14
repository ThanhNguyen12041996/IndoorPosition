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
        // double exp = ( 27.55 - (20*Math.log10(FreqInDb)) +6.7+ Math.abs(signLevelInDb)) / 50;
        //double exp = ( 22.4255 + Math.abs(signLevelInDb) -20 * Math.log10(FreqInDb)) / 29;  // for 2412 MHZ
        //  double exp = ( 18.3035 + Math.abs(signLevelInDb) -20 * Math.log10(FreqInDb)) / 29;  // for 2437 MHZ
        double exp = ( 7.148996586 + Math.abs( signLevelInDb) - 40.0459702) / 50;
        return Math.pow(10.0,exp);
    }
}
package com.example.minhthanh.mapslocation;

/**
 * Created by minh thanh on 5/24/2018.
 */

public interface IShowDirection {
    void SendDirection(String source, String destination);
    void SendRemoveFragment();
    void SendLocationToDetermine(String nodeLocation);
}

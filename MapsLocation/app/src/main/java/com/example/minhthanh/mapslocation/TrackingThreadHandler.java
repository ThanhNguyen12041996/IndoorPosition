package com.example.minhthanh.mapslocation;
import android.net.wifi.WifiManager;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Created by minh thanh on 3/12/2018.
 */

public class TrackingThreadHandler
{
    ObjectInputStream InputStream;
    PrintWriter OutPutStream;
    WifiManager wifimanager;

    public TrackingThreadHandler(final WifiManager wifimanager)
    {
        this.wifimanager = wifimanager;
    }
    public void ThreadTracking(final EditText editText)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket connectionSocket = new Socket(editText.getText().toString(), 9999);
                    InputStream = new ObjectInputStream(connectionSocket.getInputStream());
                    OutPutStream = new PrintWriter(connectionSocket.getOutputStream());

                 /*   while (true) {

                        TimeUnit.SECONDS.sleep(4);
                        wifimanager.startScan();
                        Log.d("TimeStart:",""+System.currentTimeMillis());
                    }*/

                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } /*catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
        t.start();
    }

    public void Send(String DataArray)
    {
        OutPutStream.println(DataArray);
        OutPutStream.flush();
    }
}
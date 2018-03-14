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

public class LocalizationThreadHandler
{
    ObjectInputStream InputStream;
    PrintWriter OutPutStream;
    android.os.Handler handler = new android.os.Handler();
    WifiManager wifimanager;

    public LocalizationThreadHandler(final WifiManager wifimanager)
    {
        this.wifimanager = wifimanager;
    }

    public void ThreadLocalization(final EditText editText)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket connectionSocket = new Socket(editText.getText().toString(), 9999);
                    InputStream = new ObjectInputStream(connectionSocket.getInputStream());
                    OutPutStream = new PrintWriter(connectionSocket.getOutputStream());

                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        t.start();
    }

    public void Send(String android_id, long timeStamp, int rss)
    {
         OutPutStream.println("RSS;" + android_id + ";" + timeStamp + ";" +rss);
         OutPutStream.flush();
    }
}

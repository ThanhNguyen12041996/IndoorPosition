package com.example.minhthanh.mapslocation;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by minh thanh on 1/4/2018.
 */

public class Client  extends Thread {

    private ObjectInputStream InputStream;
    public  PrintWriter OutPutStream;
    private String android_id;
    private String Wifirouter;

    String content = "";

    Location CurrentPosition = new Location("Thanh Nguyen");
    double LatLn = 0;
    double LongLn = 0;

    Thread client;
    Socket connectionSocket = null;

    public Client(String android_id, EditText edit, String WifiRouter)  {

        this.android_id = android_id;
        this.Wifirouter = WifiRouter;
        GetData(edit);
    }

    public synchronized void GetData(final EditText edit) {

        client = new Thread() {
            @Override
            public void run() {

                try {
                    connectionSocket = new Socket(edit.getText().toString(), 9999);
                    InputStream = new ObjectInputStream(connectionSocket.getInputStream());
                    OutPutStream = new PrintWriter(connectionSocket.getOutputStream());
                    OutPutStream.println("AndroidID;" + android_id + ";" + Wifirouter);
                    OutPutStream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {

                    try {
                        content = InputStream.readObject().toString();
                        String[] Coodinates = content.split(";");
                        Log.d("Content la",content);
                        if (Coodinates[0].equals("LatLn"))
                            LatLn = Double.parseDouble(Coodinates[1]);
                        else if (Coodinates[0].equals("LongLn"))
                            LongLn = Double.parseDouble(Coodinates[1]);
                        if (Coodinates[0].equals("Finish")) {
                            Log.d("Thread2Nhanduoctoado", String.valueOf(LatLn) + "," + String.valueOf(LongLn));
                            SetLocation(LatLn, LongLn);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
         client.start();
        }

    public void SetLocation(double LatLin, double LongLin) {
        CurrentPosition.setLatitude(LatLin);
        CurrentPosition.setLongitude(LongLin);
    }

    public void SendCoordinates(String LatlnData, String LongLnData) {

        OutPutStream.println("LatLnLongLn;" +android_id+ ";" +LatlnData + ";" +LongLnData);
        OutPutStream.flush();
    }

    public Location GetLocation() {
        return CurrentPosition;
    }
}

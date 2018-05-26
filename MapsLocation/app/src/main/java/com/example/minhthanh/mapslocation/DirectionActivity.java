package com.example.minhthanh.mapslocation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DirectionActivity extends Fragment implements View.OnClickListener,IShowDistanceDuration {

    Button btnDirected;
    EditText editText_Source, editText_Destination;
    public IShowDirection sendCoordinates;
    ArrayAdapter<String> adapter;
    boolean flag = false;
    AlertDialog ad;
    String itemValue;
    ListView lv;
    String source,destination;
    TextView viewDis,viewDuration;
    Button buttonCancel;
    DecimalFormat dF = new DecimalFormat("##.#");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_direction, container, false);
        viewDis = v.findViewById(R.id.textView3);
        viewDuration = v.findViewById(R.id.textView4);
        btnDirected = v.findViewById(R.id.button3);
        editText_Source = v.findViewById(R.id.edit_textSource);
        editText_Destination = v.findViewById(R.id.edit_Destination);
        editText_Destination.setOnClickListener(this);
        buttonCancel = v.findViewById(R.id.button2);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCoordinates.SendRemoveFragment();
            }
        });
        editText_Source.setOnClickListener(this);
        btnDirected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                source = source.toLowerCase();
                destination = destination.toLowerCase();
                sendCoordinates.SendDirection(source,destination);
            }
        });
        return v;
    }

    public void InitListLocation() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater(Bundle.EMPTY);
        View convertView =  inflater.inflate(R.layout.list_location, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("University of Science");
        lv = convertView.findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, MapsActivity.ListDetail);
        lv.setAdapter(adapter);
        ad = alertDialog.show();
        ad.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemValue = (String) lv
                        .getItemAtPosition(position);
                if( flag == true){
                    source = itemValue.toString();
                    editText_Source.setText(source);
                    flag = false;}
                else{
                    destination = itemValue.toString();
                editText_Destination.setText(destination);}
                ad.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        InitListLocation();
        if( v.getId() == R.id.edit_textSource)
            flag = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            sendCoordinates = (IShowDirection) context;
        }catch (ClassCastException e){
        }
    }

    @Override
    public void SendData(double Distance, float Duration) {
        viewDis.setText(dF.format(Distance)+"m");
        viewDuration.setText(dF.format(Duration)+" mins");
    }
}

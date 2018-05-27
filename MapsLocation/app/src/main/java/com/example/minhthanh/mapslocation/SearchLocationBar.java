package com.example.minhthanh.mapslocation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import static com.example.minhthanh.mapslocation.MapsActivity.ListDetail;

/**
 * Created by minh thanh on 5/27/2018.
 */

public class SearchLocationBar extends android.support.v4.app.Fragment  {
    SearchView searchView;
    ListView lv;
    ArrayAdapter<String> adapter;
    String itemValue;
    public IShowDirection sendCoordinates;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_listsearch,container,false);
        lv = v.findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, MapsActivity.ListDetail);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemValue = (String) lv
                        .getItemAtPosition(position);
                itemValue = itemValue.toLowerCase();
                sendCoordinates.SendLocationToDetermine(itemValue);
                sendCoordinates.SendRemoveFragment();

            }
        });
        searchView = v.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> tempList = new ArrayList<String>();
                for( String item: ListDetail)
                {

                    if( item.toLowerCase().contains(newText.toLowerCase()))
                        tempList.add(item);
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tempList);
                lv.setAdapter(adapter);
                return true;
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            sendCoordinates = (IShowDirection) context;
        }catch (ClassCastException e){
        }
    }

}

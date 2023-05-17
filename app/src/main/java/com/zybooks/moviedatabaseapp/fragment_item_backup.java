package com.zybooks.moviedatabaseapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class fragment_item_backup extends Fragment implements AdapterView.OnItemClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item,container,false);
        //return super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] city = {"Mumbai","Delhi","Bangalore","Hyderabad","Chennai","Kolkata","Pune","Surat","Jaipur","Kanpur","Lucknow"};

        ListView listView=(ListView)view.findViewById(R.id.lst);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,city);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(position == 0)
        {
            Toast.makeText(getActivity(),"Mumbai",Toast.LENGTH_SHORT).show();
        }
        if(position == 1)
        {
            Toast.makeText(getActivity(),"Delhi",Toast.LENGTH_SHORT).show();
        }
        if(position == 2)
        {
            Toast.makeText(getActivity(),"Bangalore",Toast.LENGTH_SHORT).show();
        }
        if(position == 3)
        {
            Toast.makeText(getActivity(),"Hyderabad",Toast.LENGTH_SHORT).show();
        }
        if(position == 4)
        {
            Toast.makeText(getActivity(),"Chennai",Toast.LENGTH_SHORT).show();
        }
        if(position == 5)
        {
            Toast.makeText(getActivity(),"Kolkata",Toast.LENGTH_SHORT).show();
        }
        if(position == 6)
        {
            Toast.makeText(getActivity(),"Pune",Toast.LENGTH_SHORT).show();
        }
        if(position == 7)
        {
            Toast.makeText(getActivity(),"Surat",Toast.LENGTH_SHORT).show();
        }
        if(position == 8)
        {
            Toast.makeText(getActivity(),"Jaipur",Toast.LENGTH_SHORT).show();
        }
        if(position == 9)
        {
            Toast.makeText(getActivity(),"Kanpur",Toast.LENGTH_SHORT).show();
        }
        if(position == 10)
        {
            Toast.makeText(getActivity(),"Lucknow",Toast.LENGTH_SHORT).show();
        }

    }
//    private String readFromInternalFile() throws IOException {
//        FileInputStream inputStream = openFileInput("todofile");
//        StringBuilder stringBuilder = new StringBuilder();
//        String lineSeparator = System.getProperty("line.separator");
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line).append(lineSeparator);
//            }
//        }
//
//        return stringBuilder.toString();
//    }




}

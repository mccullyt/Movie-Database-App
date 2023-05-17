package com.zybooks.moviedatabaseapp;
import android.content.Context;
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
import java.util.LinkedList;
import java.util.List;


public class fragment_item extends Fragment implements AdapterView.OnItemClickListener {
    Context thisContext;
    List<String> watchlist = new LinkedList<String>();
    String[] arr;


    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item,container,false);
        //return super.onCreateView(inflater, container, savedInstanceState);
        thisContext = container.getContext();

        try {
            readFromInternalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //String[] city = {"Mumbai","Delhi","Bangalore","Hyderabad","Chennai","Kolkata","Pune","Surat","Jaipur","Kanpur","Lucknow"};
       arr = new String[watchlist.size()];
        for (int i = 0; i < watchlist.size(); i++)
            arr[i] = watchlist.get(i);
        ListView listView=(ListView)view.findViewById(R.id.lst);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,arr);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
        Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).closeFragment(); //updateMovie(arr[position])
        ((MainActivity) getActivity()).updateMovie(arr[position]);
    }
    private String readFromInternalFile() throws IOException {
        FileInputStream inputStream = thisContext.openFileInput("watchlist");
        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //stringBuilder.append(line).append(lineSeparator);
                watchlist.add(line);
            }


        }


        return stringBuilder.toString();
    }




}

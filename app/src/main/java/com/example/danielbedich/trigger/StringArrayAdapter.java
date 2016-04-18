package com.example.danielbedich.trigger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pandyman on 3/22/2016.
 */
public class StringArrayAdapter extends BaseAdapter{

    ArrayList<String> triggerList;
    Context ctxt;
    LayoutInflater inflater;


    public StringArrayAdapter(ArrayList<String> triggerArrayList, Context context){
        this.triggerList = triggerArrayList;
        this.ctxt = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return triggerList.size();
    }
    @Override
    public Object getItem(int position){
        return triggerList.get(position);
    }
    @Override
    public long getItemId(int arg0){
        return arg0;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent){
        //add cell to activity list

        if(view==null){
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView name = (TextView)view.findViewById(android.R.id.text1);
            name.setText(triggerList.get(position));
        }

        return view;
    }
}

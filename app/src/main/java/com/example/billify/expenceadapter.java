package com.example.billify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class expenceadapter extends BaseAdapter {
    private ArrayList<Friend> friends;
    private Context appicationContext;
    private LayoutInflater inflater;

    public expenceadapter(Context appicationContext, ArrayList<Friend> friends)
    {
        this.friends=friends;
        this.appicationContext=appicationContext;
        inflater=LayoutInflater.from(appicationContext);
    }
    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= inflater.inflate(R.layout.expence,null);
        TextView tx = view.findViewById(R.id.textView);
        tx.setText(friends.get(position).getName());



        return view;
    }
}
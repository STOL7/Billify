package com.example.billify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class expenceadapter extends BaseAdapter {
    private List<String> name;
    private Context appicationContext;
    private LayoutInflater inflater;
    public expenceadapter(Context appicationContext, List<String> name)
    {
        this.name=name;
        this.appicationContext=appicationContext;
        inflater=LayoutInflater.from(appicationContext);
    }
    @Override
    public int getCount() {
       return name.size();
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
        tx.setText(name.get(position));



        return view;
    }
}

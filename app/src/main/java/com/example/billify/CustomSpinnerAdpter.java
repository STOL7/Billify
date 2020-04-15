package com.example.billify;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class CustomSpinnerAdpter extends ArrayAdapter<CustomSpinnerItem> {
    public CustomSpinnerAdpter(@NonNull Context context, ArrayList<CustomSpinnerItem> customList) {
        super(context, 0, customList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return CustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return CustomView(position, convertView, parent);
    }

    public View CustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner,parent,false);
        }
        CustomSpinnerItem items = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.imagespinner);
        TextView spinnerText = convertView.findViewById(R.id.textspinner);
        if(items != null)
        {
            spinnerImage.setImageResource(items.getSpinnerImg());
            spinnerText.setText(items.getSpinnerText());
        }
        return convertView;

    }
}

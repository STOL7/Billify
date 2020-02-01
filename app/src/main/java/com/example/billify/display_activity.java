package com.example.billify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class display_activity extends Fragment
{
    int i=0;

    private Toolbar toolbar;

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutmanager;
private TextView txt;


    activityAdapter adapt = new activityAdapter();

    ArrayList<History> histories = new ArrayList<History>();
    //MyAdapter adapt = new MyAdapter();



    Intent intent;
    FloatingActionButton f_action_btn;

    Context con;
    public display_activity()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        con=getActivity();

        layoutmanager=new LinearLayoutManager(con);

        return inflater.inflate(R.layout.recyclerview, container, false);

    }



    public void onStart()
    {

        super.onStart();



    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerview = (RecyclerView) getView().findViewById(R.id.recycler_view);
        txt=(TextView)getView().findViewById(R.id.no_birthday);
        txt.setText(getString(R.string.no_birthday_found));
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutmanager);

        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
      DatabaseHelper db = new DatabaseHelper(con);
        db.createDataBase();
        db.openDataBase();


        histories = db.geHistory();

        adapt = new activityAdapter(histories);
        recyclerview=(RecyclerView)getView().findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);

        recyclerview.setAdapter(adapt);






    }

    }











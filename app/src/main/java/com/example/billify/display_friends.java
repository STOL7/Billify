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


public class display_friends extends Fragment
{
    int i=0;

    //DatabaseReference databaseReference;

    private Toolbar toolbar;

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutmanager;
private TextView txt;


    MyAdapter adapt = new MyAdapter();
    List<String> items= new ArrayList<>();
    private ArrayList<String> profile = new ArrayList<String>();
    public ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> bdate = new ArrayList<String>();
  private ArrayList<Friend> friends = new ArrayList<>();
    //MyAdapter adapt = new MyAdapter();



    Intent intent;
    FloatingActionButton f_action_btn;

    Context con;
    public display_friends()
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
        f_action_btn = (FloatingActionButton)getView().findViewById(R.id.fab);

        f_action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myIntent = new Intent(getActivity(), AddExpenseActivity.class);
                getActivity().startActivity(myIntent);


            }
        });





        DatabaseHelper db = new DatabaseHelper(con);
        db.createDataBase();
        db.openDataBase();
        friends = db.geFriend();

        Friend fd = friends.get(0);
        friends.remove(0);


        fd.setId(fd.getId());
        fd.setBalance(fd.getBalance());
        fd.setProfile(fd.getProfile());
        fd.setContact(fd.getContact());
        fd.setName(fd.getName());
        fd.setEmail(fd.getEmail());




        if(friends.size() > 0)
       {
           txt.setVisibility(View.INVISIBLE);
       }

        adapt=new MyAdapter(friends);
        ContactAdapter cadpt = new ContactAdapter(friends);
        final Billify bf=(Billify) getActivity().getApplicationContext();

        bf.setAdpt(adapt);
        bf.setYou(fd);
        bf.setCadpt(cadpt);
        recyclerview=(RecyclerView)getView().findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);

        recyclerview.setAdapter(adapt);






    }

    }











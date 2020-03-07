package com.example.billify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class group extends Fragment {
    Intent intent;
    FloatingActionButton f_action_btn;

    Context con;
    private Toolbar toolbar;
    ArrayList<Friend> groups = new ArrayList<Friend>();

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    GroupAdapter adapt = new GroupAdapter();
    private RecyclerView.LayoutManager layoutmanager;
    private TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        con = getActivity();

        layoutmanager = new LinearLayoutManager(con);

        return inflater.inflate(R.layout.recyclerview, container, false);

    }

    public void onStart() {

        super.onStart();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerview = (RecyclerView) getView().findViewById(R.id.recycler_view);
        txt = (TextView) getView().findViewById(R.id.no_birthday);
        txt.setText(getString(R.string.no_birthday_found));
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutmanager);

        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        f_action_btn = (FloatingActionButton) getView().findViewById(R.id.fab);

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

        groups = db.geFriend();

        adapt = new GroupAdapter(groups);


        recyclerview = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);

        recyclerview.setAdapter(adapt);
    }
}

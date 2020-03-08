package com.example.billify;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;


public class billDetail extends AppCompatActivity
{
    private Toolbar toolbar;

    private History history;
    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    public ArrayList<history_membor> histories;
    private RecyclerView.LayoutManager layoutmanager;
    private TextView title,amount,date;

    private billAdapter adapt;




    public void onCreate(Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bill_detail);


        layoutmanager=new LinearLayoutManager(this);

        title = (TextView) findViewById(R.id.txttitle);
        amount =(TextView) findViewById(R.id.txtmoney);
        date  =(TextView) findViewById(R.id.txtdate);

        history = (History) getIntent().getSerializableExtra("user");

        title.setText(history.getTitle());
        date.setText(history.getDate());
        amount.setText(history.getAmount()+"");
        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutmanager);

        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    DatabaseHelper db = new DatabaseHelper(this);
        db.createDataBase();
        db.openDataBase();


        histories = db.geHistoryMember(history.getId());

        for(int i=0;i<histories.size();i++)
        {
            histories.get(i).setName(db.getMemberName(histories.get(i).getId()));
        }


        adapt = new billAdapter(histories);


        recyclerview.setAdapter(adapt);
    }
    public void onStart()
    {

        super.onStart();



    }


}

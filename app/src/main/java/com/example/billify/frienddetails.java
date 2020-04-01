package com.example.billify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class frienddetails extends AppCompatActivity
{
    private RecyclerView recyclerview;
    private Friend friend;
    private History history;
    private ArrayList<friend_history> fh;
    friend_historyAdapter adapter;
    String id1;

    private RecyclerView.LayoutManager layoutmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienddetails);

        layoutmanager=new LinearLayoutManager(this);
        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutmanager);

        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        friend = (Friend) getIntent().getSerializableExtra("user");

        DatabaseHelper db = new DatabaseHelper(this);
        db.createDataBase();
        db.openDataBase();

        final Billify bf=(Billify)getApplicationContext();
        id1 = bf.getYou().getId();

        fh = db.getFriendHistory(id1,friend.getId());

        adapter = new friend_historyAdapter(fh,friend.getName());
        recyclerview.setAdapter(adapter);
        //history = db.getHistory(friend.getId());


    }
}

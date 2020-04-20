package com.example.billify;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class groupdetails extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerview;
    private Group group;
    PieChart pieChart;
    private TextView title, date;
    FloatingActionButton f_action_btn;
    activityAdapter adapt;
    private ArrayList<Friend> gm;
    MyAdapter adapter;
    DatabaseHelper db;
    ArrayList<History> histories = new ArrayList<History>();


    private RecyclerView.LayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdetails);

        layoutmanager = new LinearLayoutManager(this);


        title = (TextView) findViewById(R.id.txttitle);

        date = (TextView) findViewById(R.id.txtdate);

        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutmanager);

        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        group = (Group) getIntent().getSerializableExtra("group");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         db = new DatabaseHelper(this);
        db.createDataBase();
        db.openDataBase();

        final Billify bf = (Billify) getApplicationContext();

        f_action_btn = (FloatingActionButton) findViewById(R.id.fab);

        f_action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bf.setSelected(null);

                Intent myIntent = new Intent(groupdetails.this, AddExpenseActivity.class);
                myIntent.putExtra("group", group.getId());
                startActivity(myIntent);


            }
        });


        title.setText(group.getName());
        date.setText(group.getDate());

        adapt = new activityAdapter(db.getGroupHistory(group.getId()));

        recyclerview.setAdapter(adapt);
        //history = db.getHistory(friend.getId());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.group, menu);

        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        if (id == R.id.show_member)
        {
            gm = db.getGroupMembers(group.getId());

            adapter = new MyAdapter(gm);

            recyclerview.setAdapter(adapter);
        }
        return true;
    }
}

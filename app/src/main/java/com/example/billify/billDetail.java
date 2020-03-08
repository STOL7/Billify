package com.example.billify;

import android.content.Context;
import android.graphics.Color;
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


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class billDetail extends AppCompatActivity
{
    private Toolbar toolbar;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;

    ArrayList pieEntries;
    ArrayList PieEntryName;
    private History history;
    private history_membor member;
    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    public ArrayList<history_membor> histories;
    private RecyclerView.LayoutManager layoutmanager;
    private TextView title,amount,date;
    String nm;
    private billAdapter adapt;




    public void onCreate(Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bill_detail);

        pieChart = findViewById(R.id.pieChart);
        PieEntryName = new ArrayList();
        pieEntries = new ArrayList<>();
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
            member = histories.get(i);
            pieEntries.add(new PieEntry(Float.parseFloat(member.getExpense()), 0));

            nm = db.getMemberName(member.getId());
            member.setName(nm);
            PieEntryName.add(nm);
        }


        adapt = new billAdapter(histories);


        recyclerview.setAdapter(adapt);

        pieDataSet = new PieDataSet(pieEntries, "No of Members");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
    }
    public void onStart()
    {

        super.onStart();



    }


}

package com.example.billify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
    public ArrayList<friend_history> netdata;
    private RecyclerView.LayoutManager layoutmanager;
    private TextView title,amount,date;
    String nm;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    DatabaseHelper db;
    AddExpenseActivity ex;
    private billAdapter adapt;
     Billify bf;
     String uid,hid;



    public void onCreate(Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bill_detail);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);
        firestore = FirebaseFirestore.getInstance();

        pieChart = findViewById(R.id.pieChart);
        PieEntryName = new ArrayList();
        pieEntries = new ArrayList<>();
        layoutmanager=new LinearLayoutManager(this);

        ex = new AddExpenseActivity();
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
        toolbar = findViewById(R.id.toolbar);
        bf = (Billify)getApplicationContext();

        uid=bf.you.getId();
        hid=history.getId();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        db= new DatabaseHelper(this);
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
    public boolean deleteHistory()
    {
        firestore.collection("Users").document(uid).
                collection("Transactions").document(history.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete history", "DocumentSnapshot successfully deleted!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Fail delete", "Error deleting document", e);
                    }
                });
        return true;

    }


    public void onStart()
    {

        super.onStart();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.bill_menu,menu);

        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        int id = item.getItemId();


        if (id == R.id.edit)
        {
            Intent intent  = new Intent(billDetail.this,AddExpenseActivity.class);
            //intent.putExtra("user",user);
            startActivity(intent);

            finish();
        }
        else if(id == R.id.delete)
        {
            //Toast.makeText(this,""+uid,Toast.LENGTH_LONG).show();
            int j= db.delete(history.getId());
            String k,k1;
            Long am;
            friend_history fs;
            if(j != 0)
            {


                this.deleteHistory();
                netdata = db.getFriendForDelete(hid);
                if(netdata != null)
                {
                    for(int i=0;i<netdata.size();i++)
                    {
                        fs = netdata.get(i);
                        k = fs.getUser_id();
                        k1= fs.getOpp_id();
                        am = fs.getPaid();

                        ex.getForNetUpdate(k,k1,-am);
                        ex.getForNetUpdate(k1,k,am);
                    }

                    db.delete(hid);
                }


               // bdr.setFlag(j);
                Toast.makeText(this,"Item Successfully deleted",Toast.LENGTH_LONG).show();
                finish();

            }
            else
            Toast.makeText(this,"unsuccessfully",Toast.LENGTH_LONG).show();
        }
        else
        {

        }
        return super.onOptionsItemSelected(item);
    }

}

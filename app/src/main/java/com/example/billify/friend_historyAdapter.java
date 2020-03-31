package com.example.billify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class friend_historyAdapter extends RecyclerView.Adapter<friend_historyAdapter.MyViewHolder> implements Serializable
{


    public ArrayList<friend_history> fiter;

    String name;

    public ArrayList<friend_history> histories;


    public friend_historyAdapter(ArrayList<friend_history> his,String name)
    {
        this.fiter=his;
        this.name = name;
        this.histories=his;
    }

    public friend_historyAdapter()
    {

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_history, parent, false);

        Log.d("ActivityViewHolder",itemView.toString());

        //notifyDataSetChanged();
        return new MyViewHolder(itemView);


    }



    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i)
    {

        final int j=i;
        String tt=name;
        final Billify bf=(Billify)getApplicationContext();
        if((bf.getYou().getId()).equals(histories.get(i).getUser_id()))
        {
            tt=bf.getYou().getName();
        }


        final Context context=myViewHolder.rlt.getContext();
        long exp = histories.get(i).getPaid();
        //final String dd= histories.get(i).getPaid();

        //myViewHolder.date.setText(dd);
        myViewHolder.expense.setText(exp+"");
        myViewHolder.title.setText(tt);


        myViewHolder.rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseHelper db = new DatabaseHelper(context);
                db.createDataBase();
                db.openDataBase();

                final History hs =  db.getHistory1(histories.get(i).getHistory_id());
               Intent itn =  new Intent(context,billDetail.class);
               itn.putExtra("user", (Serializable)hs);

               context.startActivity(itn);

            }
        });


    }

    @Override
    public int getItemCount()
    {
        return histories.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {


        TextView title;
        TextView date;
        TextView expense;

        RelativeLayout rlt;
        public MyViewHolder(View itemView)
        {
            super(itemView);



            date=(TextView)itemView.findViewById(R.id.date);
            title=(TextView)itemView.findViewById(R.id.titl);
            expense=(TextView)itemView.findViewById(R.id.expense);
            rlt=(RelativeLayout)itemView.findViewById(R.id.rlt);


        }


    }



}

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class billAdapter extends RecyclerView.Adapter<billAdapter.MyViewHolder> implements Serializable
{


    public ArrayList<history_membor> fiter;


    public ArrayList<history_membor> histories;


    public billAdapter(ArrayList<history_membor> his)
    {
        this.fiter=his;
        this.histories=his;
    }

    public billAdapter()
    {

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_details, parent, false);

        Log.d("ActivityViewHolder",itemView.toString());

        //notifyDataSetChanged();
        return new MyViewHolder(itemView);


    }



    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i)
    {

        final int j=i;
        final String tt=histories.get(i).getName();
//        String em=emails.get(i);

        String img="";
        //long exp = histories.get(i).getAmount();
        long exp = Long.parseLong(histories.get(i).getExpense());
        final String dd= histories.get(i).getPaid();
        //final String dd=histories.get(i).getDate();


        final Context context=myViewHolder.img.getContext();

        if(img.equals(""))
        {
            myViewHolder.img.setImageResource(R.drawable.profile);
        }
        else
        {
            String imageDataBytes = img.substring(img.indexOf(",")+1);

            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

            Bitmap bitmap = BitmapFactory.decodeStream(stream);

            myViewHolder.img.setImageBitmap(bitmap);
        }

        //startAppAd= new StartAppAd(context);
        myViewHolder.date.setText(dd);
        myViewHolder.expense.setText(exp+"");
        myViewHolder.title.setText(tt);


        myViewHolder.rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             //   Intent itn =  new Intent(context,billDetail.class);
              //  itn.putExtra("user", (Serializable) histories.get(i));

               // context.startActivity(itn);

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

        ImageView img;
        //TextView email;
        TextView title;
        TextView date;
        TextView expense;

        RelativeLayout rlt;
        public MyViewHolder(View itemView)
        {
            super(itemView);


            img=(ImageView)itemView.findViewById(R.id.bill);
            // email=(TextView)itemView.findViewById(R.id.em);
            date=(TextView)itemView.findViewById(R.id.date);
            title=(TextView)itemView.findViewById(R.id.titl);
            expense=(TextView)itemView.findViewById(R.id.expense);
            rlt=(RelativeLayout)itemView.findViewById(R.id.rlt);


        }


    }



}

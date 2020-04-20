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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> implements Serializable
{



    public ArrayList<Group> fiter;


    public ArrayList<Group> groups;

    public GroupAdapter(ArrayList<Group> groups)
    {
        this.groups=groups;
        this.fiter=groups;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.birthday_view, parent, false);

        Log.d("onCreateViewHolder",itemView.toString());

        //notifyDataSetChanged();
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i)
    {
        //String im=img.get(i);
        final int j=i;
        final String nm=groups.get(i).getName();
//        String em=emails.get(i);

        String img=groups.get(i).getImage();
        String net_expense=groups.get(i).getDate();
        final String bd="";

        final Context context=myViewHolder.img.getContext();
        if(img.equals("") || img == null)
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
        myViewHolder.bdate.setText(bd);
        myViewHolder.names.setText(nm);
        myViewHolder.net.setText(net_expense+"");

        myViewHolder.rlt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent itn =  new Intent(context,groupdetails.class);
                itn.putExtra("group",  groups.get(i));

                context.startActivity(itn);

            }
        });







    }

    @Override
    public int getItemCount()
    {
        return groups.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        //TextView email;
        TextView names;
        TextView bdate;
        TextView net;

        RelativeLayout rlt;
        public MyViewHolder(View itemView)
        {
            super(itemView);

            img=(ImageView)itemView.findViewById(R.id.profile);

            bdate=(TextView)itemView.findViewById(R.id.bdate);
            names=(TextView)itemView.findViewById(R.id.name);
            net = (TextView) itemView.findViewById(R.id.net_expense);
            rlt=(RelativeLayout)itemView.findViewById(R.id.rlt);


        }


    }



}

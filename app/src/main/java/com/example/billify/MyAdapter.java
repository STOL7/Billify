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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Serializable
{



    public ArrayList<Friend> fiter;


    public ArrayList<Friend> friends;

    public MyAdapter(ArrayList<Friend> friends)
    {
        this.friends=friends;
        this.fiter=friends;

    }

    public MyAdapter()
    {

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
        final String nm=friends.get(i).getName();
//        String em=emails.get(i);

        String img=friends.get(i).getProfile();
        long net_expense=friends.get(i).getBalance();
        final String bd=friends.get(i).getContact();

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
        myViewHolder.bdate.setText(bd);
        myViewHolder.names.setText(nm);
        myViewHolder.net.setText(net_expense+"");

        myViewHolder.rlt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent itn =  new Intent(context,frienddetails.class);
                itn.putExtra("user",  friends.get(i));

                context.startActivity(itn);

            }
        });







    }

    @Override
    public int getItemCount()
    {
        return friends.size();
    }
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if (str.isEmpty())
                {
                    friends = fiter;

                } else {
                    ArrayList<Friend> filteredList = new ArrayList<>();
                    for (Friend birth : fiter) {

                        if ((birth.getBalance()+"").toLowerCase().contains(str) || birth.getName().toLowerCase().contains(str) ||
                                birth.getEmail().toLowerCase().contains(str) || birth.getContact().toLowerCase().contains(str)) {

                            filteredList.add(birth);
                        }
                        friends = filteredList;
                    }



                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = friends;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                friends = (ArrayList<Friend>) results.values;
                notifyDataSetChanged();
            }
        };
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
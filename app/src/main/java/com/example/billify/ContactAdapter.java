package com.example.billify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> implements Serializable
{



    public ArrayList<Friend> fiter;


    public ArrayList<Friend> friends;

    public ContactAdapter(ArrayList<Friend> friends)
    {
        this.friends=friends;
        this.fiter=friends;

    }

    public ContactAdapter()
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
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i)
    {
        //String im=img.get(i);
        final int j=i;
        final String nm=friends.get(i).getName();
//        String em=emails.get(i);

        String img=friends.get(i).getProfile();
        final String bd=friends.get(i).getContact();

        final String id =friends.get(i).getId();
        final Context context=myViewHolder.img.getContext();

       // if(img.equals(""))
       // {
            myViewHolder.img.setImageResource(R.drawable.profile);
       // }
        /*else
        {
            String imageDataBytes = img.substring(img.indexOf(",")+1);

            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

            Bitmap bitmap = BitmapFactory.decodeStream(stream);

            myViewHolder.img.setImageBitmap(bitmap);
        }*/

        myViewHolder.bdate.setText(bd);
        myViewHolder.names.setText(nm);
        final Billify bf=(Billify) getApplicationContext();
        if(bf.getSelected()!=null && bf.getSelected().contains(friends.get(i)))
        {
            myViewHolder.rlt.setAlpha((float)0.5);
        }
        myViewHolder.rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                //View alertLayout = inflater.inflate(R.layout.recyclerview, null);


               ArrayList<Friend> av = bf.getSelected();
               if(av == null)
                   av = new ArrayList<Friend>();

               if(myViewHolder.rlt.getAlpha() == 0.5)
               {
                   av.remove(friends.get(i));
                   bf.getSelected().remove(friends.get(i));
                   myViewHolder.rlt.setAlpha((float)1);
               }
               else
               {


                   if(!av.contains(friends.get(i)))
                   {
                       av.add(friends.get(i));
                       bf.setSelected(av);
                   }

                   myViewHolder.rlt.setAlpha((float)0.5);
               }
            }
        });







    }

    @Override
    public int getItemCount()
    {
        return friends.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        //TextView email;
        TextView names;
        TextView bdate;

        RelativeLayout rlt;
        public MyViewHolder(View itemView)
        {
            super(itemView);

            img=(ImageView)itemView.findViewById(R.id.profile);

            bdate=(TextView)itemView.findViewById(R.id.bdate);
            names=(TextView)itemView.findViewById(R.id.name);
            rlt=(RelativeLayout)itemView.findViewById(R.id.rlt);


        }


    }



}

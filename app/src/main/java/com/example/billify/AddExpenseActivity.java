package com.example.billify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference databaseReference;
    EditText discription,title,participate,amount,f_name,f_email,f_contact;
    Spinner category,billed_by,split;
    Toolbar toolbar,toolbar1;;
    Button add_new,add_exp;
    TextView billbytext;
    TextView billtypetext;
    ImageView ln_image;
    ArrayList<Friend> par_friends = new ArrayList<Friend>();
    String image="";
    RecyclerView recyclerview;
    Uri imgUri;
    long balance_fire=0;
    DatabaseHelper db;
    int count=0;
    int width;
    int dpwidth,dpheight;
    int height;
    int dpi;
    GridView grid;
    LinearLayout ly;
    double pcount=0;
    int numcolume;
    int numrow=1;
    int size=80;
    double total;
    private  ImageView im;
    int nu=0;
    FirebaseFirestore firestore;
    private static Calendar calender;
    FloatingActionButton bill_image;
    int uneqflag=0;
    HashMap<String,Integer> paid_hash;
    HashMap<String,Long> give_hash;
    HashMap<String,Integer> boorow_hash;
    int sz;
    Friend you;
    String youid;
    int[] split_arr;
    int[] paid_arr;
    private Spinner categorySpinner;
    String gid="";
    ArrayList<CustomSpinnerItem> customList;
    CustomSpinnerAdpter customSpinnerAdpter;
    String BillBy[] = {
            "You",
            "Multiple",

    };
    String type[] = {
            "Equally",
            "UnEqually",

    };
    ArrayAdapter AdpterParticipate,AdpterType;
    String text;
    List<String> participatelist,TypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        dpi=dm.densityDpi;
        //dpheight=height*160/dpi;
        dpwidth=(width*160)/dpi;
        final int num=dpwidth/140;


       Button b =(Button) findViewById(R.id.night);
        Button p =(Button) findViewById(R.id.popup);

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ShowPopUp();
            }


        });

       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
               //startActivity(new Intent(getApplicationContext(),AddExpenseActivity.class));
               //finish();
           }
       });
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);
        firestore =FirebaseFirestore.getInstance();

        discription = (EditText)findViewById(R.id.discription);
        title = (EditText)findViewById(R.id.title);
        participate=(EditText)findViewById(R.id.participate);
        amount = (EditText)findViewById(R.id.amount);

        category = (Spinner)findViewById(R.id.category);
        billed_by = (Spinner)findViewById(R.id.paid);
        split = (Spinner)findViewById(R.id.split);
        db = new DatabaseHelper(this);
        add_new=(Button)findViewById(R.id.add_new);
        add_exp=(Button)findViewById(R.id.add_exp);
        categorySpinner = (Spinner) findViewById(R.id.category);


        billbytext = (TextView) findViewById(R.id.billbytext);
        billtypetext =(TextView) findViewById(R.id.billtypetext);

        paid_hash = new HashMap<String, Integer>();
        boorow_hash = new HashMap<String, Integer>();
        give_hash = new HashMap<String, Long>();
        im = (ImageView) findViewById(R.id.titleimage);
        sz= par_friends.size();
        participate.setText("");
        bill_image=(FloatingActionButton)findViewById(R.id.bill_image);
        ln_image=(ImageView)findViewById(R.id.ln_image);
        toolbar = findViewById(R.id.toolbar);
        final Billify bf=(Billify)getApplicationContext();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        you = bf.getYou();
        youid=you.getId();
        par_friends.add(you);
        ly = (LinearLayout) findViewById(R.id.f1);
        grid = findViewById(R.id.grid1);
        grid.setNumColumns(num);



        participatelist =  new ArrayList(Arrays.asList(BillBy));
        TypeList = new ArrayList(Arrays.asList(type));

        AdpterParticipate = new ArrayAdapter(this,R.layout.spinnercontent,participatelist);
        AdpterType = new ArrayAdapter(this,R.layout.spinnercontent, TypeList);

        AdpterParticipate.setDropDownViewResource(R.layout.spinnercontent);
        AdpterType.setDropDownViewResource(R.layout.spinnercontent);



        final expenceadapter[] expenceadapter = {new expenceadapter(this, par_friends)};

       customList= new ArrayList<>();
        customList.add(new CustomSpinnerItem("",R.mipmap.title));
        customList.add(new CustomSpinnerItem("Games",R.mipmap.game));
        customList.add(new CustomSpinnerItem("Movies",R.mipmap.movie));
        customList.add(new CustomSpinnerItem("Music",R.mipmap.music));
        customList.add(new CustomSpinnerItem("Sports",R.mipmap.sport));
        customList.add(new CustomSpinnerItem("Dining Out",R.mipmap.diningout));
        customList.add(new CustomSpinnerItem("Groceries",R.mipmap.shopping));
        customList.add(new CustomSpinnerItem("Liquor",R.mipmap.drinks));
        customList.add(new CustomSpinnerItem("Electronics",R.mipmap.electonics));
        customList.add(new CustomSpinnerItem("Furniture",R.mipmap.furniture));
        customList.add(new CustomSpinnerItem("HouseHold Supplies",R.mipmap.homeoutsupplies));
        customList.add(new CustomSpinnerItem("Maintenance",R.mipmap.maintanance));
        customList.add(new CustomSpinnerItem("Pets",R.mipmap.pet));
        customList.add(new CustomSpinnerItem("Rent",R.mipmap.rent));
        customList.add(new CustomSpinnerItem("Service",R.mipmap.service));
        customList.add(new CustomSpinnerItem("ChildCare",R.mipmap.childcare));
        customList.add(new CustomSpinnerItem("Clothing",R.mipmap.clothing));
        customList.add(new CustomSpinnerItem("Education",R.mipmap.education));
        customList.add(new CustomSpinnerItem("Gifts",R.mipmap.gift));
        customList.add(new CustomSpinnerItem("Insurance",R.mipmap.insurance));
        customList.add(new CustomSpinnerItem("Medical Expances",R.mipmap.medical));
        customList.add(new CustomSpinnerItem("Taxes",R.mipmap.taxes));
        customList.add(new CustomSpinnerItem("Bicycle",R.mipmap.bycyle));
        customList.add(new CustomSpinnerItem("Bus/Train",R.mipmap.bus));
        customList.add(new CustomSpinnerItem("Car",R.mipmap.car));
        customList.add(new CustomSpinnerItem("Fule",R.mipmap.fule));
        customList.add(new CustomSpinnerItem("Hotel",R.mipmap.hotel));
        customList.add(new CustomSpinnerItem("Parking",R.mipmap.parking));
        customList.add(new CustomSpinnerItem("Plane",R.mipmap.plane));
        customList.add(new CustomSpinnerItem("Taxi",R.mipmap.taxi));
        customList.add(new CustomSpinnerItem("Cleaning",R.mipmap.cleaning));
        customList.add(new CustomSpinnerItem("Electricity",R.mipmap.elecricity));
        customList.add(new CustomSpinnerItem("Heat/gas",R.mipmap.gas));
        customList.add(new CustomSpinnerItem("Trash",R.mipmap.trash));
        customList.add(new CustomSpinnerItem("TV/Phone/Internet",R.mipmap.wifi));
        customList.add(new CustomSpinnerItem ("Water",R.mipmap.water));

         customSpinnerAdpter= new CustomSpinnerAdpter(this,customList);


        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySpinner.performClick();

              //  Object ob = new CustomSpinnerItem ("Water",R.mipmap.water);
                //customList.remove(ob);

            }
        });



        if(getIntent().getStringExtra("group") != null)
        {
            gid = getIntent().getStringExtra("group");

            par_friends.addAll(db.getGroupMembers(gid));

            expenceadapter[0] = new expenceadapter(AddExpenseActivity.this,par_friends);


            total = par_friends.size();

            pcount = total/num;

            pcount = Math.ceil(pcount);
            ly.setMinimumHeight((dpi*42/160)*(int)pcount);
            //Toast.makeText(AddExpenseActivity.this,""+dpheight+" "+dpi,Toast.LENGTH_LONG).show();
            grid.setAdapter(expenceadapter[0]);
            bf.setSelected(par_friends);
        }


        billtypetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                split.performClick();
            }
        });
        billbytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               billed_by.performClick();
            }
        });

         if(split!=null)
         {
             split.setAdapter(AdpterType);
             split.setOnItemSelectedListener(this);
         }

         if(billed_by!=null)
         {
             billed_by.setAdapter(AdpterParticipate);
             billed_by.setOnItemSelectedListener(this);
         }
        if (categorySpinner!=null)
        {
            categorySpinner.setAdapter(customSpinnerAdpter);
            categorySpinner.setOnItemSelectedListener(this);

        }



        split.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1)
                {


                    split_arr = popup();

                }
                billtypetext.setText(split.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        billed_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1)
                {



                    paid_arr =  popup();

                }
                billbytext.setText(billed_by.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdpterParticipate.remove(par_friends.get(position).getName());

                par_friends.remove(position);
                expenceadapter[0] = new expenceadapter(AddExpenseActivity.this,par_friends);
                grid.setAdapter(expenceadapter[0]);
                total = par_friends.size();
                pcount = total/num;
                pcount = Math.ceil(pcount);
                ly.setMinimumHeight((dpi*42/160)*(int)pcount);

                AdpterParticipate.notifyDataSetChanged();
                billed_by.setAdapter(AdpterParticipate);



            }
        });

        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.showcontact, null);
                toolbar1 = alertLayout.findViewById(R.id.toolbar1);
                setSupportActionBar(toolbar1);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                recyclerview = (RecyclerView) alertLayout.findViewById(R.id.recycler_view);
                recyclerview.setHasFixedSize(true);
                recyclerview.setLayoutManager(new LinearLayoutManager(AddExpenseActivity.this));

                recyclerview.addItemDecoration(new DividerItemDecoration(AddExpenseActivity.this, LinearLayoutManager.VERTICAL));
                recyclerview.setAdapter(bf.getCadpt());

                // participate.setText(participate.getText() + bf.getSelected());
                AlertDialog dialog = new AlertDialog.Builder(AddExpenseActivity.this)

                        .setView(alertLayout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if(!bf.getSelected().contains(you))
                                {
                                    bf.getSelected().add(you);
                                }

                                par_friends = bf.getSelected();
                                        for(Friend f:par_friends)
                                        {
                                             if(!participatelist.contains(f.getName())) {
                                                 participatelist.add(f.getName());
                                             }
                                        }
                                        AdpterParticipate.notifyDataSetChanged();
                                        billed_by.setAdapter(AdpterParticipate);
                                       expenceadapter[0] = new expenceadapter(AddExpenseActivity.this,par_friends);


                                total = par_friends.size();

                                pcount = total/num;

                                pcount = Math.ceil(pcount);
                                ly.setMinimumHeight((dpi*42/160)*(int)pcount);
                                //Toast.makeText(AddExpenseActivity.this,""+dpheight+" "+dpi,Toast.LENGTH_LONG).show();
                                grid.setAdapter(expenceadapter[0]);


                            }
                        }).setNegativeButton("cancle",null).create();

                dialog.show();




            }
        });
        add_exp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String tt = String.valueOf(title.getText());
                String categ = category.getSelectedItem().toString();
                String dis = String.valueOf(discription.getText());

                String amo = String.valueOf(amount.getText());

                int devide=0;
                String uuid = UUID.randomUUID().toString();
                String uuid1,details_id;
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);

                int size_jk = par_friends.size();

                if(amo.length() == 0)
                {
                    Toast.makeText(AddExpenseActivity.this,"Please enter amount",Toast.LENGTH_LONG).show();
                }


                else
                {
                    int total = Integer.parseInt(amo);
                    if(billed_by.getSelectedItemPosition() == 1 || billed_by.getSelectedItemPosition() == 0)
                    {
                        paid_arr = new int[size_jk];
                        paid_arr[size_jk-1] = Integer.parseInt(amo);
                        for(int i=0;i<size_jk-1;i++)
                        {
                            paid_arr[i]=0;
                        }
                    }


                    if(split.getSelectedItemPosition() == 0)
                    {
                        split_arr = new int[size_jk];
                        devide = Integer.parseInt(amo)/size_jk;
                        for(int i=0;i<size_jk;i++)
                        {
                            split_arr[i]=devide;
                        }
                    }

                    if(implementHashing(split_arr,paid_arr,size_jk))
                    {
                        if(db.addExpense(uuid,dis,tt,categ,total,thisDate,image,gid))
                        {
                            addExpenseToFirestore(uuid,dis,tt,categ,total,image,gid);

                            for(int i=0;i<size_jk;i++)
                            {
                                details_id =  UUID.randomUUID().toString();

                                db.history_details(details_id,uuid,par_friends.get(i).getId(),paid_arr[i],split_arr[i]);
                                addDetailsToFirestore(uuid,par_friends.get(i).getId(),paid_arr[i],split_arr[i]);
                            }

                            Toast.makeText(AddExpenseActivity.this,"Successfully inserted",Toast.LENGTH_LONG).show();

                            Iterator myVeryOwnIterator = paid_hash.keySet().iterator();
                            Iterator borro_itr = boorow_hash.keySet().iterator();
                            Iterator give_itr = give_hash.keySet().iterator();


                            String key=(String)myVeryOwnIterator.next();
                            int value=(int)paid_hash.get(key);

                            String key1 =(String)borro_itr.next();
                            int value1=(int)boorow_hash.get(key1);
                            int net=0;



                            while(true)
                            {

                                uuid1 = UUID.randomUUID().toString();
                                net = value - value1;

                                if(net <= 0)
                                {

                                    db.addIndivisual(uuid1,uuid,key,key1,value);
                                    addOwesToFirestore(uuid,key,key1,value);

                                    if(key1.equals(youid) || key.equals(youid))
                                    {
                                        db.updateExpense(key1,give_hash.get(key1)+value);
                                        db.updateExpense(key,give_hash.get(key)-value);
                                    }

                                    getForNetUpdate(key,key1,value);

                                    getForNetUpdate(key1,key,-value);



                                }
                                else
                                {
                                    db.addIndivisual(uuid1,uuid,key,key1,value1);
                                    addOwesToFirestore(uuid,key,key1,value1);
                                    if(key1.equals(youid) || key.equals(youid))
                                    {
                                        db.updateExpense(key1,give_hash.get(key1)+value1);

                                        db.updateExpense(key,give_hash.get(key)-value1);

                                    }

                                    getForNetUpdate(key,key1,value1);
                                    getForNetUpdate(key1,key,-value1);




                                }

                                if(net < 0 )
                                {
                                    value1 = value1 -value;
                                    boorow_hash.put(key1,value1);
                                }
                                else if(borro_itr.hasNext())
                                {
                                    key1 =(String)borro_itr.next();
                                    Log.d("dsd",key1);
                                    value1=(int)boorow_hash.get(key1);
                                }
                                else
                                {
                                    key1="";
                                    value1=0;
                                }
                                if(net > 0 && value1 != 0)
                                {
                                    value = net;
                                    paid_hash.put(key,net);
                                }
                                else if(myVeryOwnIterator.hasNext())
                                {
                                    key =(String)myVeryOwnIterator.next();
                                    //Log.d("dsd",key);
                                    value=(int)paid_hash.get(key);


                                }
                                else
                                    break;

                            }

                        }
                    }

                }




            }
        });


        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.add_new, null);

                f_contact = (EditText)alertLayout.findViewById(R.id.f_contact);
                f_name = (EditText)alertLayout.findViewById(R.id.f_name);
                f_email = (EditText)alertLayout.findViewById(R.id.f_email);

                AlertDialog dialog = new AlertDialog.Builder(AddExpenseActivity.this)
                        .setTitle("New Connection")
                        .setView(alertLayout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String name = String.valueOf(f_name.getText());
                                final String email = String.valueOf(f_email.getText());
                                final String contact = String.valueOf(f_contact.getText());

                                if(name.length() == 0)
                                {
                                    Toast.makeText(AddExpenseActivity.this,"Please enter name",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    if(contact.length() == 0)
                                    {
                                        if(email.length() == 0)
                                        {

                                            Toast.makeText(AddExpenseActivity.this,"Please enter contact no or email",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Pattern p = Pattern.compile("[a-zA-Z0-9._-]+@[a" +
                                                    "-z]+\\.+[a-z]+");

                                            Matcher m = p.matcher(email);
                                            if(!m.find())
                                            {
                                                Toast.makeText(AddExpenseActivity.this,"Please enter valid email",Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                final String nm=name;
                                                final String em=email;

                                                final String[][] uid = {new String[5]};

                                                if(db.findByEmail(email))
                                                    Toast.makeText(AddExpenseActivity.this,"Already available your friend list",Toast.LENGTH_LONG).show();
                                                else
                                                {
                                                    databaseReference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot)
                                                        {



                                                            if(dataSnapshot.getValue() == null)
                                                            {
                                                                //send request for add new friend on server
                                                                if(db.addNew(email,nm,em,"",0,""))
                                                                {

                                                                    addToFirestore(youid,email,0);
                                                                    addToFirestore(email,youid,0);

                                                                    Intent share = new Intent(Intent.ACTION_SEND);
                                                                    share.setType("text/plain");
                                                                    String shareBody ="Please install Billify app, " +
                                                                            " https://play.google.com/store/apps/details?id="+getPackageName();
                                                                    share.putExtra(Intent.EXTRA_SUBJECT, "Billify");
                                                                    share.putExtra(Intent.EXTRA_TITLE, "Billify");
                                                                    // share.setData();
                                                                    share.putExtra(Intent.EXTRA_TEXT, shareBody);
                                                                    startActivity(Intent.createChooser(share, "Share via"));

                                                                    Toast.makeText(AddExpenseActivity.this,"Send request to friend",Toast.LENGTH_LONG).show();
                                                                }


                                                            }
                                                            else
                                                            {
                                                                for(DataSnapshot data:dataSnapshot.getChildren())
                                                                {
                                                                    Log.i("result",data.toString());

                                                                    String key = data.getKey().toString();
                                                                    String  cn = data.child("Contact").getValue().toString();
                                                                    String profile = data.child("Profile").getValue().toString();
                                                                    //String Name = data.child("Name").getValue().toString();
                                                                    if(cn == null)
                                                                        cn="";
                                                                    if(profile == null)
                                                                        profile="";
                                                                    if(db.addNew(key,name,email,cn,0,profile))
                                                                    {

                                                                        addToFirestore(youid,key,0);

                                                                        addToFirestore(key,youid,0);



                                                                    }


                                                                }




                                                            }


                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError)
                                                        {


                                                        }
                                                    });

                                                }




                                            }
                                        }

                                    }
                                    else
                                    {
                                        if(db.findByContact(contact))
                                            Toast.makeText(AddExpenseActivity.this,"All ready your friend",Toast.LENGTH_LONG).show();
                                        else
                                        {
                                            final String nm=name;
                                            final String cn=contact;

                                            final String[][] uid = {new String[5]};
                                            databaseReference.orderByChild("Contact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if (dataSnapshot.getValue() == null)
                                                    {
                                                        if (db.addNew(contact, nm, "", cn, 0, ""))
                                                        {
                                                            addToFirestore(youid,contact,0);
                                                            addToFirestore(contact,youid,0);
                                                            Toast.makeText(AddExpenseActivity.this, "Send request by  contact", Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                    else
                                                    {
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            Log.i("result", data.toString());

                                                            String key = data.getKey().toString();
                                                            String em = data.child("Email").getValue().toString();
                                                            String profile = data.child("Profile").getValue().toString();
                                                            //String Name = data.child("Name").getValue().toString();
                                                            if (em == null)
                                                                em = "";
                                                            if (profile == null)
                                                                profile = "";
                                                            if (db.addNew(key, name, em, contact, 0, profile)) {

                                                                addToFirestore(youid, key,0);
                                                                addToFirestore(key, youid,0);


                                                            }

                                                        }

                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError)
                                                {

                                                }
                                            });

                                        }

                                    }
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {         @Override
        public void onClick(View v)
        {
            finish();
        }
        });

        bill_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ContextCompat.checkSelfPermission(AddExpenseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    SelectImage();
                }
                else
                {
                    ActivityCompat.requestPermissions(AddExpenseActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

    }




    public long getForNetUpdate(final String key, final String key1, final long value)
    {

        firestore.collection("Users").document(key)
                .collection("Friends").document(key1)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Map<String, Object> forms = document.getData();
                        balance_fire =Long.parseLong(forms.get("Balance").toString());
                        Log.d("get data", "DocumentSnapshot data: " + balance_fire);
                        addToFirestore(key,key1,balance_fire+value);
                    }
                    else
                    {
                        addToFirestore(key,key1,value);
                        Log.d("no data", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        return balance_fire;
    }

    public void addDetailsToFirestore( String uuid, String id, int i, int i1)
    {
        Map<String, Object> transaction = new HashMap<>();

        transaction.put("paid", i);
        transaction.put("expense", i1);


        firestore.collection("Users").document(youid).
                collection("Transactions").document(uuid).
                collection("Participates").document(id).
                set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("success details", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail", "Error writing document", e);
                    }
                });
    }


    private void addOwesToFirestore( String uuid, String id,String opposite, int i)
    {
        Map<String, Object> transaction = new HashMap<>();

        transaction.put("paid", i);



        firestore.collection("Users").document(youid).
                collection("Transactions").document(uuid).
                collection("Participates").document(id).
                collection("Owe").document(opposite).
                set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("success details", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail", "Error writing document", e);
                    }
                });
    }


    private void addExpenseToFirestore(String uuid, String dis,
                                       String tt, String categ, int total, String image, String gid)
    {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("amount", total);

        transaction.put("date",new Date());
        transaction.put("billImage", image);
        transaction.put("groupId", gid);
        transaction.put("description", dis);
        transaction.put("title", tt);
        transaction.put("category", categ);



        firestore.collection("Users").document(youid).
                collection("Transactions").document(uuid).
                set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("success", "Transaction done successfully");
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.w("fail", "Error writing document", e);
                    }
                });


    }

    private void addToFirestore(String keyz, String youidz,long balance)
    {
        Map<String, Object> user = new HashMap<>();
        user.put("Balance", balance);

        firestore.collection("Users").document(keyz).
                collection("Friends").document(youidz).
                set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("success ", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.w("fail", "Error writing document", e);
                    }
                });

    }

    private boolean implementHashing(int[] split_arr, int[] paid_arr,int size)
    {
        for(int i=0;i<size;i++)
        {
            int net = paid_arr[i] - split_arr[i];

            give_hash.put(par_friends.get(i).getId(),par_friends.get(i).getBalance());
            if(net > 0)
            {
                paid_hash.put(par_friends.get(i).getId(),net);


                //Log.d(net+" hj"+i,par_friends.get(i).getId()+"fgdg"+paid_hash.get(par_friends.get(i).getId())+"");
            }
            else {
                net=   split_arr[i] - paid_arr[i];
                boorow_hash.put(par_friends.get(i).getId(), net);
                // Log.d(net+" hj12"+i,"hhjf "+par_friends.get(i).getId()+" jk" +boorow_hash.get(par_friends.get(i).getId())+"");
            }




        }


        return true;

        /**/
    }

    public int[] popup()
    {

        sz = par_friends.size();
        final int[] arr = new int[sz];
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.unequall, null);

        final LinearLayout myRoot = (LinearLayout)alertLayout.findViewById(R.id.uneq);

        myRoot.removeAllViews();
        TextView[] tx = new TextView[sz];
        final EditText[] ex = new EditText[sz];
        final int count=0;
        for(int i=0;i<sz;i++)
        {
            LinearLayout ln = new LinearLayout(AddExpenseActivity.this);
            ln.setGravity(Gravity.CENTER);
            ln.setOrientation(LinearLayout.HORIZONTAL);
            ln.setId(i);

            tx[i] = new TextView(AddExpenseActivity.this);
            ex[i] = new EditText(AddExpenseActivity.this);



            ex[i].setId(i);
            tx[i].setId(i);

            tx[i].setText(par_friends.get(i).getName());

            ln.addView(tx[i]);
            ln.addView(ex[i]);
            myRoot.addView(ln);


        }

        AlertDialog dialog = new AlertDialog.Builder(AddExpenseActivity.this)

                .setView(alertLayout)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {


                        int sum=0;
                        int c;
                        for(int i=0;i<sz;i++)
                        {
                            if(ex[i].getText().length() > 0)
                                c = Integer.parseInt(String.valueOf(ex[i].getText()));
                            else
                                c=0;
                            arr[i] =c;
                            sum =sum+c;
                            //Log.d("msg",arr[i]+"");
                        }
                        if(sum != Integer.parseInt(String.valueOf(amount.getText())))
                        {
                            uneqflag=1;
                            Toast.makeText(getApplicationContext(), "Provide valid value", Toast.LENGTH_LONG).show();
                        }





                    }
                }).setNegativeButton("cancle",null).create();

        dialog.show();




        return arr;

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 9) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                SelectImage();
            else
            {
                SelectImage();
                //Toast.makeText(this, "Please provide permission to choose image", Toast.LENGTH_LONG).show();
            }


        } else if (requestCode == 70 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // SelectContact();

        } else
            Toast.makeText(this, "Please provide permission to access contacts", Toast.LENGTH_LONG).show();
    }

    public void SelectImage()
    {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 86);
    }

    public void SelectContact()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,70);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 86 && data!= null )
        {
            imgUri = data.getData();

            try
            {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // In case you want to compress your image, here it's at 40%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();


                long lengthbmp = byteArray.length;




                if(lengthbmp > 200000)
                {
                    AlertDialog dialog = new AlertDialog.Builder(AddExpenseActivity.this)

                            .setMessage("Image size should be less than 500KB")
                            .create();
                    dialog.show();
                    imgUri=null;


                }
                else
                {
                    image = Base64.encodeToString(byteArray, Base64.DEFAULT);



                    String imageDataBytes = image.substring(image.indexOf(",")+1);

                    InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

                    Bitmap bitmap1 = BitmapFactory.decodeStream(stream);




                    ln_image.setImageBitmap(bitmap1);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            //name.setText(data.getData().getPath()+"");
        }

       /* else if(requestCode == 70 && data!= null )
        {
            Uri contactData = data.getData();

            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                try {
                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        String cNumber = phones.getString(phones.getColumnIndex("data1"));
                        //System.out.println("number is:" + cNumber);
                        contact.setText(cNumber);
                    }
                    String name1 = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    name.setText(name1);
                }
                catch (Exception ex)
                {
                    ex.getMessage();
                }
            }
        }*/
        else
        {

        }


    }
    private void ShowPopUp() {

        final Dialog tempDilog = new Dialog(AddExpenseActivity.this);
        tempDilog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tempDilog.setContentView(R.layout.addfriendpopup);
        Button add = tempDilog.findViewById(R.id.add);

        f_contact = (EditText)tempDilog.findViewById(R.id.f_contact);
        f_name = (EditText)tempDilog.findViewById(R.id.f_name);
        f_email = (EditText)tempDilog.findViewById(R.id.f_email);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = String.valueOf(f_name.getText());
                final String email = String.valueOf(f_email.getText());
                final String contact = String.valueOf(f_contact.getText());

                if(name.length() == 0)
                {
                    Toast.makeText(AddExpenseActivity.this,"Please enter name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(contact.length() == 0)
                    {
                        if(email.length() == 0)
                        {

                            Toast.makeText(AddExpenseActivity.this,"Please enter contact no or email",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Pattern p = Pattern.compile("[a-zA-Z0-9._-]+@[a" +
                                    "-z]+\\.+[a-z]+");

                            Matcher m = p.matcher(email);
                            if(!m.find())
                            {
                                Toast.makeText(AddExpenseActivity.this,"Please enter valid email",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                final String nm=name;
                                final String em=email;

                                final String[][] uid = {new String[5]};

                                if(db.findByEmail(email))
                                    Toast.makeText(AddExpenseActivity.this,"Already available your friend list",Toast.LENGTH_LONG).show();
                                else
                                {
                                    databaseReference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {



                                            if(dataSnapshot.getValue() == null)
                                            {
                                                //send request for add new friend on server
                                                if(db.addNew(email,nm,em,"",0,""))
                                                {

                                                    addToFirestore(youid,email,0);
                                                    addToFirestore(email,youid,0);
                                                    Toast.makeText(AddExpenseActivity.this,"Send request to friend",Toast.LENGTH_LONG).show();
                                                }


                                            }
                                            else
                                            {
                                                for(DataSnapshot data:dataSnapshot.getChildren())
                                                {
                                                    Log.i("result",data.toString());

                                                    String key = data.getKey().toString();
                                                    String  cn = data.child("Contact").getValue().toString();
                                                    String profile = data.child("Profile").getValue().toString();
                                                    //String Name = data.child("Name").getValue().toString();
                                                    if(cn == null)
                                                        cn="";
                                                    if(profile == null)
                                                        profile="";
                                                    if(db.addNew(key,name,email,cn,0,profile))
                                                    {

                                                        addToFirestore(youid,key,0);

                                                        addToFirestore(key,youid,0);

                                                    }
                                                }
                                            }

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError)
                                        {

                                        }
                                    });
                                }

                            }
                        }

                    }
                    else
                    {
                        if(db.findByContact(contact))
                            Toast.makeText(AddExpenseActivity.this,"All ready your friend",Toast.LENGTH_LONG).show();
                        else
                        {
                            final String nm=name;
                            final String cn=contact;

                            final String[][] uid = {new String[5]};
                            databaseReference.orderByChild("Contact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getValue() == null)
                                    {
                                        if (db.addNew(contact, nm, "", cn, 0, ""))
                                        {
                                            addToFirestore(youid,contact,0);
                                            addToFirestore(contact,youid,0);
                                            Toast.makeText(AddExpenseActivity.this, "Send request by  contact", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    else
                                    {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            Log.i("result", data.toString());

                                            String key = data.getKey().toString();
                                            String em = data.child("Email").getValue().toString();
                                            String profile = data.child("Profile").getValue().toString();
                                            //String Name = data.child("Name").getValue().toString();
                                            if (em == null)
                                                em = "";
                                            if (profile == null)
                                                profile = "";
                                            if (db.addNew(key, name, em, contact, 0, profile)) {

                                                addToFirestore(youid, key,0);
                                                addToFirestore(key, youid,0);


                                            }

                                        }

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {

                                }
                            });

                        }

                    }
                }


            }
        });
        Button cancle = tempDilog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDilog.cancel();
            }
        });

        tempDilog.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu);

        //SearchView sv = (SearchView)menu.findItem(R.id.search).getActionView();


        return super.onCreateOptionsMenu(menu);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.search)
        {


            SearchView sv = (SearchView)item.getActionView();

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s)
                {

                    return  false;
                }

                @Override
                public boolean onQueryTextChange(String s)
                {
                    final Billify bd=(Billify) getApplicationContext();
                    bd.getCadpt().getFilter().filter(s);

                    return true;
                }
            });


        }

//        else if (id == R.id.action_refresh)
//        {
//            checkDevice();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

        if(adapterView.getId()==R.id.category) {
            CustomSpinnerItem items = (CustomSpinnerItem) adapterView.getSelectedItem();
            int n = items.getSpinnerImg();

            items.getSpinnerImg();
            im.setImageResource(items.getSpinnerImg());

            title.setText(items.getSpinnerText());
        }
        if(adapterView.getId()==R.id.paid)

        {

        }
        if(adapterView.getId()==R.id.split)

        {

        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}


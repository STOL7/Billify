package com.example.billify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class addInGroup extends AppCompatActivity
{

    Toolbar toolbar,toolbar1;
    EditText title,discription,participate;
    int width,total,dpwidth,height,dpi;
    RecyclerView recyclerview;

    DatabaseHelper db ;
    Button create;
    Friend you;
    FloatingActionButton f_action_btn;
    String tt,ds,id1,dd;
    Uri imgUri;
    ImageView profile;
    String id,date,image;
    ArrayList<Friend> par_friends = new ArrayList<Friend>();
    GridView grid;
    List<String> participatelist,TypeList;
    LinearLayout ly;
    FirebaseFirestore firestore;
    double pcount=0;
    FloatingActionButton group_image;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_in_group);

         firestore = FirebaseFirestore.getInstance();

        image="";

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        dpi=dm.densityDpi;
        dpwidth=(width*160)/dpi;
        final int num=dpwidth/140;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        discription = (EditText)findViewById(R.id.discription);
        title = (EditText)findViewById(R.id.title);
        participate = (EditText)findViewById(R.id.participate);
        profile = (ImageView)findViewById(R.id.group_profile) ;
        create = (Button)findViewById(R.id.create);
        group_image = (FloatingActionButton) findViewById(R.id.group_image);
        ly = (LinearLayout) findViewById(R.id.f1);

        grid = findViewById(R.id.grid1);
        grid.setNumColumns(num);

        final Billify bf=(Billify)getApplicationContext();
        you = bf.getYou();
        par_friends.add(you);

        db = new DatabaseHelper(this);
        db.createDataBase();
        db.openDataBase();


        participatelist =  new ArrayList();

        final expenceadapter[] expenceadapter = {new expenceadapter(this, par_friends)};


        f_action_btn= (FloatingActionButton)findViewById(R.id.group_image);

        f_action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SelectImage();


            }
        });



        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                par_friends.remove(position);
                expenceadapter[0] = new expenceadapter(addInGroup.this,par_friends);
                grid.setAdapter(expenceadapter[0]);
                total = par_friends.size();
                pcount = total/num;
                pcount = Math.ceil(pcount);
                ly.setMinimumHeight((dpi*42/160)*(int)pcount);





            }
        });

        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                id = UUID.randomUUID().toString();
                Date todayDate = new Date();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                date = currentDate.format(todayDate);

                dd = date;
                tt = title.getText().toString();
                ds = discription.getText().toString();
                db.newGroup(id,tt,ds,dd,image);
                Map<String, Object> group = new HashMap<>();
                Map<String, Object> nt = new HashMap<>();
                group.put("title", tt);
                group.put("discription", ds);
                group.put("date", date);
                group.put("image", image);



                firestore.collection("Groups").document(id).
                        set(group).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Group added", "successfully");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("fail to add in group", "Error writing document", e);
                            }
                        });

                int i=0;
                for( i =0;i<par_friends.size();i++)
                {
                    id1 = par_friends.get(i).getId();
                    db.groupMember(id,id1);
                    firestore.collection("Users").document(id1).
                            collection("Groups").document(id).set(nt).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Group added", "users account");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("fail to add in group", "Error writing document", e);
                                }
                            });

                    firestore.collection("Groups").document(id).
                            collection("Members").document(id1).set(nt).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Members  added ", "in group");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("fail to add members ", " in group Error writing document", e);
                                }
                            });
                }

                //if(i == par_friends.size())
                   // finish();
            }
        });
        participate.setOnClickListener(new View.OnClickListener()
        {
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
                recyclerview.setLayoutManager(new LinearLayoutManager(addInGroup.this));

                recyclerview.addItemDecoration(new DividerItemDecoration(addInGroup.this, LinearLayoutManager.VERTICAL));
                recyclerview.setAdapter(bf.getCadpt());


                AlertDialog dialog = new AlertDialog.Builder(addInGroup.this)

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

                                expenceadapter[0] = new expenceadapter(addInGroup.this,par_friends);


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



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 9)
        {
            SelectImage();
        } else
            Toast.makeText(this, "Please provide permission to access Image", Toast.LENGTH_LONG).show();
    }

    public void SelectImage()
    {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 86 && data != null) {
            imgUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // In case you want to compress your image, here it's at 40%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();


                long lengthbmp = byteArray.length;


                if (lengthbmp > 200000) {
                    AlertDialog dialog = new AlertDialog.Builder(addInGroup.this)

                            .setMessage("Image size should be less than 500KB")
                            .create();
                    dialog.show();
                    imgUri = null;


                } else {
                    image = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    String imageDataBytes = image.substring(image.indexOf(",") + 1);

                    InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

                    Bitmap bitmap1 = BitmapFactory.decodeStream(stream);


                    profile.setImageBitmap(bitmap1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //name.setText(data.getData().getPath()+"");
        }
    }

    }

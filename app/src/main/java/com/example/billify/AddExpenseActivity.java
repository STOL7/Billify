package com.example.billify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AddExpenseActivity extends AppCompatActivity
{

    DatabaseReference databaseReference;
    EditText discription,title,participate,amount,f_name,f_email,f_contact;
    Spinner category,billed_by,split;
    Toolbar toolbar;
    Button add_new,add_exp;
    ImageView ln_image;
    ArrayList<Friend> par_friends = new ArrayList<Friend>();
    String image="";
    RecyclerView recyclerview;
    Uri imgUri;
    DatabaseHelper db;
    private static Calendar calender;
    FloatingActionButton bill_image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);


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


        participate.setText("");
        bill_image=(FloatingActionButton)findViewById(R.id.bill_image);
        ln_image=(ImageView)findViewById(R.id.ln_image);
        toolbar = findViewById(R.id.toolbar);
        final Billify bf=(Billify)getApplicationContext();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.recyclerview, null);
                recyclerview = (RecyclerView) alertLayout.findViewById(R.id.recycler_view);
                recyclerview.setHasFixedSize(true);
                recyclerview.setLayoutManager(new LinearLayoutManager(AddExpenseActivity.this));


                recyclerview.setAdapter(bf.getCadpt());

               // participate.setText(participate.getText() + bf.getSelected());
                AlertDialog dialog = new AlertDialog.Builder(AddExpenseActivity.this)

                        .setView(alertLayout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener()
                                {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {


                                par_friends.addAll(bf.getSelected());
                                for(int i=0;i<bf.getSelected().size();i++)
                                {
                                    participate.setText(participate.getText() + ", " + bf.getSelected().get(i).getName());
                                }
                                bf.setSelected(null);
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
                String by = billed_by.getSelectedItem().toString();
                int total = Integer.parseInt(amo);
                int devide= total / (par_friends.size()+1);
                String uuid = UUID.randomUUID().toString();
                String uuid1;
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);
                if(db.addExpense(uuid,total,thisDate,image,0))
                {
                    Toast.makeText(AddExpenseActivity.this,"Successfully inserted",Toast.LENGTH_LONG).show();
                    for(int j=0;j<par_friends.size();j++)
                    {
                        uuid1 = UUID.randomUUID().toString();
                        Friend gh=par_friends.get(j);
                        if(db.addIndivisual(uuid1,uuid,gh.getId(),0,devide,0))
                            continue;
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
                                String email = String.valueOf(f_email.getText());
                                String contact = String.valueOf(f_contact.getText());

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
                                                   Toast.makeText(AddExpenseActivity.this,"All ready your friend",Toast.LENGTH_LONG).show();
                                               else
                                               {
                                                   databaseReference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(DataSnapshot dataSnapshot)
                                                       {


                                                           if(dataSnapshot.getValue() == null)
                                                           {
                                                               //send request for add new friend on server
                                                               if(db.addNew(nm,em,""))
                                                               {
                                                                   Toast.makeText(AddExpenseActivity.this,"Success",Toast.LENGTH_LONG).show();
                                                               }


                                                           }
                                                           else
                                                           {
                                                               String key = dataSnapshot.getValue().toString();
                                                               uid[0] = key.split("\\{");
                                                               Toast.makeText(AddExpenseActivity.this,"get data + " + uid[0][1],Toast.LENGTH_LONG).show();


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
                                                public void onDataChange(DataSnapshot dataSnapshot)
                                                {

                                                    if(dataSnapshot.getValue() == null)
                                                    {
                                                        if(db.addNew(nm,"",cn))
                                                        {
                                                            Toast.makeText(AddExpenseActivity.this,"Success contact",Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                    else
                                                    {
                                                        String key = dataSnapshot.getValue().toString();
                                                        uid[0] = key.split("\\{");
                                                        Toast.makeText(AddExpenseActivity.this,"get contact data + " + dataSnapshot.getValue(),Toast.LENGTH_LONG).show();


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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
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






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 9) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                SelectImage();
            else
                Toast.makeText(this, "Please provide permission to choose image", Toast.LENGTH_LONG).show();

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



    public void UploadData()
    {

        /*u_bDate = bDate.getText().toString();
        u_name = name.getText().toString();
        Pattern p = Pattern.compile("^([0-2][0-9]|(3)[0-1]|[0-9])(\\/)(((0)[0-9])|((1)[0-2]|[0-9]))(\\/)\\d{4}$");

        Matcher m = p.matcher(u_bDate);
        u_contact = contact.getText().toString();
        u_email = email.getText().toString();
        DatabaseHelper db = new DatabaseHelper(this);
        if(name.getText().length() == 0)
            Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();

        else if(bDate.getText().length() == 0)
            Toast.makeText(this,"Please select the birthdate ",Toast.LENGTH_LONG).show();

        else if(!m.find())
        {
            Toast.makeText(this,"Date should be in form of dd/mm/yyyy only",Toast.LENGTH_LONG).show();
        }
        else if(user == null)
        {
            //Toast.makeText(this," "+type,Toast.LENGTH_LONG).show();
            if(db.insertData(u_name,u_email,u_contact,profile_img,u_bDate,"",type))
            {
                bd.setFlag(1);
                finish();
            }
            else
                Toast.makeText(this,"Unsuccess"+u_bDate+u_contact,Toast.LENGTH_LONG).show();
        }
        else
        {
            if(db.updateData(u_name,u_email,u_contact,profile_img,u_bDate,user.getId(),type))
            {
                bd.setFlag(1);
                finish();
            }
            else
                Toast.makeText(this,"Unsuccess"+u_bDate+u_contact,Toast.LENGTH_LONG).show();
        }*/
    }
}

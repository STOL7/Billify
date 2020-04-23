package com.example.billify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MessagingService extends FirebaseMessagingService
{

    FirebaseFirestore firestore;
    Friend you;
    String tt,dd,dt,img,mid,gid,id1;
    Map<String,Object> docu;
    String[] mids;

    private DatabaseReference databaseReference;
    List<String> coll = new ArrayList<>();
    String uuid;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);


        String title = remoteMessage.getNotification().getTitle();

        final Map<String, String> Message = remoteMessage.getData();
        firestore =FirebaseFirestore.getInstance();

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        final Billify bf=(Billify)getApplicationContext();
        you = bf.getYou();

        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.createDataBase();
        db.openDataBase();
        //Toast.makeText(this,title,Toast.LENGTH_LONG);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.baseline_backup_black_36)
                .setContentTitle("New Group Created")
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setAutoCancel(true)

                .setContentText("Updated new group");



        Intent todayBirthdayIntent = new Intent(this, WelcomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, todayBirthdayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);


            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);

            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }



        if(title.equals("New Group"))
        {
            //Toast.makeText(this,"came into the picure",Toast.LENGTH_LONG);


            tt = Message.get("title");
            dd = Message.get("discription");
            dt = Message.get("date");
            img = Message.get("image");
            mid = Message.get("memberIds");
            gid = Message.get("group_id");


            // Log.d("Members is ",mid);
//            Log.d("title is",tt);


            if (Message.get("title") != null && dd != null && dt != null && img != null && mid != null && gid != null)
            {
                mids = mid.split("/");
                Log.d("Length of Members is ", mids.length + "");

                Group gp = new Group();
                gp.setDate(dt);
                gp.setDescription(dd);
                gp.setImage(img);
                gp.setId(gid);
                gp.setName(tt);
                if (!bf.getGroup().contains(gp))
                {


                    for (int i = 0; i < mids.length; i++)
                    {
                        id1 = mids[i];
                        Log.d(you.getId(), id1);

                        db.newGroup(gid, tt, dd, dt, img);

                        Log.d("Members is ", id1);
                        db.groupMember(gid, id1);

                    }









                    notificationManager.notify(1, builder.build());


                }
            }

        }

        if(title.equals("New Friend"))
        {
            final String fid = Message.get("friendId");
            Log.d("Friend request received",fid);

            databaseReference.child(fid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    String nm = (String) dataSnapshot.child("Name").getValue();
                    String pr = (String) dataSnapshot.child("Profile").getValue();
                    String em = (String) dataSnapshot.child("Email").getValue();
                    String cn = (String) dataSnapshot.child("Contact").getValue();
                    String bl =  (String) dataSnapshot.child("Balance").getValue();




                    if(db.findByEmail(em))
                    {
                        Toast.makeText(getApplicationContext(), "Already you friend" , Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        if(db.addNew(fid, nm, em, cn, 0, pr));
                        Toast.makeText(getApplicationContext(), "Added to your list" , Toast.LENGTH_LONG).show();
                    }




                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {


                }
            });


            builder.setContentTitle("New Friend  added to your list");
            builder.setContentText("Updated new friend");

            notificationManager.notify(2, builder.build());
        }
       if(title.equals("New transction"))
        {
            final String uid= Message.get("user_id");
            final String hid= Message.get("transaction_id");

            Log.d("Amount received",uid);



            if(!you.getId().equals(Message.get("user_id")))
            {







                builder.setContentTitle("New Expense added");
                builder.setContentText("Updated new group");





                notificationManager.notify(3, builder.build());


                db.addExpense(hid,Message.get("description"),Message.get("title"),
                        Message.get("category"),Integer.parseInt(Message.get("amount")),
                        Message.get("date"),Message.get("billImage"),Message.get("groupId"));


                firestore.collection("Users").document(uid).
                        collection("Transactions").document(hid).
                        collection("Participates")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult())
                                    {
                                        uuid = UUID.randomUUID().toString();
                                        docu = document.getData();
                                        final String src = document.getId();
                                        //coll.add(did[0]);
                                        boolean b = db.history_details(uuid, hid,src,
                                                Integer.parseInt(docu.get("paid").toString()),Integer.parseInt(docu.get("expense").toString()));

                                        firestore.collection("Users").document(uid).
                                                collection("Transactions").document(hid).
                                                collection("Participates").document(src).collection("Owe")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (final QueryDocumentSnapshot document : task.getResult())
                                                            {
                                                                uuid = UUID.randomUUID().toString();

                                                                int j = Integer.parseInt(document.getData().get("paid").toString());

                                                                if(document.getId().equals(you.getId()))
                                                                {
                                                                    //Log.d("yes","it is "+ src);
                                                                    firestore.collection("Users").document(document.getId()).
                                                                            collection("Friends").document(uid).
                                                                            get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot)
                                                                        {

                                                                            db.updateExpense(src,Long.parseLong(documentSnapshot.get("Balance").toString()));
                                                                        }
                                                                    });


                                                                }
                                                                db.addIndivisual(uuid,hid,uid,document.getId(),j);
                                                                // Log.d("Tag",document.getId() + " => " + document.getData());
                                                            }
                                                        } else {
                                                            //Log.d("Tag", "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });


                                    }

                                } else {
                                    Log.d("Tag", "Error getting documents: ", task.getException());
                                }
                            }
                        });



            }

        }


    }

}

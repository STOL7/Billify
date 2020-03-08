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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class MessagingService extends FirebaseMessagingService
{

    FirebaseFirestore firestore;
    Friend you;
    Map<String,Object> docu;
    List<String> coll = new ArrayList<>();
    String uuid;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);


        String title = remoteMessage.getNotification().getTitle();
        final Map<String, String> Message = remoteMessage.getData();

       final String uid= Message.get("user_id");
        final String hid= Message.get("transaction_id");
        final String[] did = {""};
        Log.d("Amount received",uid);
        Log.d("billImage received",Message.get("billImage"));
        Log.d("category received",Message.get("category"));
        Log.d("description received",Message.get("description"));


        firestore =FirebaseFirestore.getInstance();

        final Billify bf=(Billify)getApplicationContext();
        you = bf.getYou();

        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.createDataBase();
        db.openDataBase();

        if(!you.getId().equals(Message.get("user_id")))
        {





            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.baseline_cake_24)
                    .setContentTitle("Today's birthday")
                    .setColor(this.getResources().getColor(R.color.colorPrimary))
                    .setAutoCancel(true)

                    .setContentText("New update");


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


            notificationManager.notify(1, builder.build());


            db.addExpense(hid,Message.get("description"),Message.get("title"),
                    Message.get("category"),Integer.parseInt(Message.get("amount")),
                    Message.get("date"),Message.get("billImage"),Integer.parseInt(Message.get("sync")));


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
                                                                        Log.d("updated to",documentSnapshot.get("Balance").toString());
                                                                        db.updateExpense(src,Long.parseLong(documentSnapshot.get("Balance").toString()));
                                                                    }
                                                                });


                                                             }
                                                             db.addIndivisual(uuid,hid,did[0],document.getId(),j,0);
                                                           // Log.d("Tag",document.getId() + " => " + document.getData());
                                                        }
                                                    } else {
                                                        //Log.d("Tag", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });

                                   // Log.d("Tag",document.getId() + " => " + document.getData() +" " +did[0]);
                                }
                                Log.d("Tag","coll size is "+ coll.size());
                            } else {
                                Log.d("Tag", "Error getting documents: ", task.getException());
                            }
                        }
                    });



        }


    }

}

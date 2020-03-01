package com.example.billify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class display_activity extends Fragment
{
    int i=0;

    private Toolbar toolbar;

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutmanager;
private TextView txt;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    History history;
   


    activityAdapter adapt = new activityAdapter();

    ArrayList<History> histories = new ArrayList<History>();
    //MyAdapter adapt = new MyAdapter();



    Intent intent;

    FloatingActionButton f_action_btn;

    Context con;
    public display_activity()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        con=getActivity();

        layoutmanager=new LinearLayoutManager(con);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();



        return inflater.inflate(R.layout.recyclerview, container, false);

    }



    public void onStart()
    {

        super.onStart();



    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        recyclerview = (RecyclerView) getView().findViewById(R.id.recycler_view);

        fStore.collection("Users").document(userId).collection("Transactions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference documentReference = fStore.collection("Users").document(userId).collection("Transactions").document(document.getId());
                        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                history = new History();
                                history.setBillIMage(documentSnapshot.getString("billImage"));
                                history.setAmount(documentSnapshot.getLong("amount"));
                                history.setTitle(documentSnapshot.getString("description"));
                                history.setDate(String.valueOf(documentSnapshot.getDate("date")));
                                addHistory(history);

                            }
                        });
                    }
                } else {
                        Toast.makeText(getActivity(),"Error is:"+task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        });







       /*txt=(TextView)getView().findViewById(R.id.no_birthday);
        txt.setText(getString(R.string.no_birthday_found));
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutmanager);
        //f_action_btn = (FloatingActionButton)getView().findViewById(R.id.fab);
        getView().findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
      DatabaseHelper db = new DatabaseHelper(con);
        db.createDataBase();
        db.openDataBase();


        histories = db.geHistory();
        if(histories.size() >0)
        {
            txt.setVisibility(View.INVISIBLE);
        }*/

       /* history = new History();
        history.setTitle("1");
        history.setDate("12");
        history.setAmount(200);
        history.setBillIMage("123");
        histories.add(history);*/

        adapt = new activityAdapter(histories);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);

        recyclerview.setAdapter(adapt);






    }
    public ArrayList<History> addHistory(History history)
    {
        Toast.makeText(getActivity(),history.getTitle(),Toast.LENGTH_LONG).show();
        histories.add(history);
        return histories;
    }

    }











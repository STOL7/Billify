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

    private static final String title[] = new String[100];
    public int a=0;
    SharedPreferences sharedPreferences;


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

       // fAuth = FirebaseAuth.getInstance();
      //  fStore = FirebaseFirestore.getInstance();


        //userId = fAuth.getCurrentUser().getUid();



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








       txt=(TextView)getView().findViewById(R.id.no_birthday);
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
        }


        adapt = new activityAdapter(histories);
        //Toast.makeText(getActivity(),String.valueOf(adapt.getItemCount()),Toast.LENGTH_LONG).show();
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);

        recyclerview.setAdapter(adapt);






    }


    }











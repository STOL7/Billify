package com.example.loginapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{

    Toolbar toolbar;
    MenuItem mitem;
    private FirebaseAuth auth;
    AccessToken accessToken;
    DrawerLayout mDrawerLayout;
  NavigationView mNavigationView;
    FirebaseAuth firebaseAuth;
    String email,fname;
    TextView tx;

    View headerView;

    Menu mn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        headerView = mNavigationView.getHeaderView(0);


        mn = mNavigationView.getMenu();
        mitem=mn.findItem(R.id.nav_item_login);
        tx = (TextView)headerView.findViewById(R.id.useremail);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();



        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);






        Toast.makeText(MainActivity.this,account.getEmail(),Toast.LENGTH_LONG).show();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                mDrawerLayout.closeDrawers();

                if(menuItem.getItemId()==R.id.nav_item_home)
                {


                }



                if(menuItem.getItemId()==R.id.nav_item_restore)
                {


                }

                if(menuItem.getItemId()==R.id.nav_item_backup)
                {




                }

                if(menuItem.getItemId()==R.id.nav_item_help)
                {

                }
                if(menuItem.getItemId()==R.id.nav_item_feedback)
                {
                    Intent Email = new Intent(Intent.ACTION_SEND);
                    Email.setType("text/email");
                    Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "17ceuog010@ddu.ac.in" });
                    Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
                    startActivity(Intent.createChooser(Email, "Send Feedback:"));
                    return true;
                }

                if(menuItem.getItemId()==R.id.nav_item_share)
                {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    String shareBody ="https://play.google.com/store/apps/details?id="+getPackageName();
                    share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Birthday Reminder");
                    share.putExtra(Intent.EXTRA_TITLE, "Birthday Reminder");
                    // share.setData();
                    share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(share, "Share via"));
                    return true;
                }
                if(menuItem.getItemId()==R.id.nav_item_login)
                {


                    if(menuItem.getTitle().equals("Logout"))
                    {
                        if(AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                            Intent marketIntent = new Intent(MainActivity.this, ChooseLoginSignupActivity.class);
                            startActivity(marketIntent);

                        }

                        else
                        {
                            FirebaseAuth.getInstance().signOut();
                            Intent marketIntent = new Intent(MainActivity.this, ChooseLoginSignupActivity.class);
                            startActivity(marketIntent);


                        }
                        mGoogleSignInClient.signOut();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                    else
                    {
                        Intent marketIntent = new Intent(MainActivity.this, SignupActivity.class);
                        startActivity(marketIntent);

                    }

                }

                if(menuItem.getItemId()==R.id.nav_item_rate)
                {

                    Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);

                }


                return false;
            }

        });


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name,
                R.string.app_name);


        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }


    @Override
    public void onStart()
    {
        super.onStart();

        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null)
        {
            tx.setText(fname);
            mitem.setTitle("Logout");
        }
        else
        {
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            if(currentUser !=null)
            {
                email = currentUser.getEmail();
                tx.setText(email);
                mitem.setTitle("Logout");


            }
        }

        }








    @Override
    protected void onStop() {
        super .onStop() ;
        //startNotification();
    }




    @Override
    protected void onPause ()
    {
        super .onPause();

        //startNotification();
    }






}

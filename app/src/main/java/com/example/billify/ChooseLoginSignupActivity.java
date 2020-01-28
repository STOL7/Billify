package com.example.billify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;

public class ChooseLoginSignupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 1001;


    private Button btnSignIn,btnSignUp;
    private SignInButton btnGoogleSignIn;
    private LoginButton btnFacebook;
    private CallbackManager callbackManager;

    private FirebaseAuth auth;
    private AccessToken accessToken;
    private GoogleApiClient googleApiClient;

    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_signup);

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);

        btnFacebook = (LoginButton) findViewById(R.id.login_button);
        btnGoogleSignIn = (SignInButton) findViewById(R.id.google_button);



        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setPermissions(Arrays.asList("email", "public_profile"));

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChooseLoginSignupActivity.this,LoginActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChooseLoginSignupActivity.this,SignupActivity.class));
            }
        });

        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {

                                } else {
                                    String email = response.getJSONObject().optString("email");
                                    String id = me.optString("id");
                                    String name = me.optString("name");


                                    new MainActivity().fname=name;

                                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(id);

                                    ref.child("Email").setValue(email);

                                    ref.child("Name").setValue(name);

                                    Toast.makeText(new ChooseLoginSignupActivity(),name,Toast.LENGTH_LONG).show();

                                    new MainActivity().tx.setText(me.optString("name"));


                                    startActivity(new Intent(ChooseLoginSignupActivity.this,MainActivity.class));
                                }
                            }
                        }).executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });



        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });




    }

    protected void addFragment(){
        /*fragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();*/

    }

    protected void replaceFragment(){
        /*fragment = new SignupFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();*/

    }





    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onStart()
    {
        super.onStart();


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null)
        {
            startActivity(new Intent(ChooseLoginSignupActivity.this,MainActivity.class));
        }
        else if(account!=null){
            startActivity(new Intent(ChooseLoginSignupActivity.this,MainActivity.class));
        }
        else
        {
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            if(currentUser !=null)
            {
                startActivity(new Intent(ChooseLoginSignupActivity.this,MainActivity.class));

            }
        }

    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String userName = account.getDisplayName().toString();
            String userEmail = account.getEmail().toString();
            String userId = account.getId().toString();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

            ref.child("Email").setValue(userEmail);

            ref.child("Name").setValue(userName);
            Toast.makeText(ChooseLoginSignupActivity.this,"sign in successfull",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(ChooseLoginSignupActivity.this,"sign in cancle",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChooseLoginSignupActivity.this,ChooseLoginSignupActivity.class));
        }

            gotoMainActivity();

    }

    private void gotoMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

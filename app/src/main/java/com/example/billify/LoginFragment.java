    package com.example.billify;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.billify.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1001;


    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private SignInButton btnGoogleSignIn;
    private LoginButton btnFacebook;
    private CallbackManager callbackManager;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private AccessToken accessToken;
    private GoogleApiClient googleApiClient;

    private GoogleSignInOptions gso;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LoginFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_login,container,false);

        auth = FirebaseAuth.getInstance();



        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.password);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnLogin = (Button) view.findViewById(R.id.sign_in_button);
        btnSignup = (Button) view.findViewById(R.id.sign_up_button);
        btnReset = (Button) view.findViewById(R.id.btn_reset_password);


        fragmentManager =getActivity().getSupportFragmentManager();

        btnFacebook = (LoginButton) view.findViewById(R.id.login_button);
        btnGoogleSignIn = (SignInButton) view.findViewById(R.id.google_button);



        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setPermissions(Arrays.asList("email", "public_profile"));


        btnSignup.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

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
                                    //FirebaseUser currentperson = FirebaseAuth.getInstance().getCurrentUser();

                                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(id);

                                    ref.child("Email").setValue(email);

                                    ref.child("Name").setValue(name);


                                    startActivity(new Intent(getActivity(),MainActivity.class));
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



        //new MainActivity().fname=name;

        //new MainActivity().tx.setText(me.optString("name"));



        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),0, (GoogleApiClient.OnConnectionFailedListener) getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null)
        {
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
        else if(account!=null){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
        else
        {
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            if(currentUser !=null)
            {
                startActivity(new Intent(getActivity(),MainActivity.class));

            }
        }



        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                checkValidation();
                break;

            case R.id.btn_reset_password:

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                new ResetPasswordFragment(),
                                "ForgotPassword_Fragment").commit();
                break;
            case R.id.sign_up_button:

                // Replace signup frgament with animation

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new SignupFragment(),
                                "SignUp_Fragment").commit();
                break;

        }


    }

    private void checkValidation(){
        String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter the password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (!task.isSuccessful()) {
                    if (password.length() < 6) {
                        inputPassword.setError(getString(R.string.minimum_password));
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        Toast.makeText(getActivity(), "Successfully login", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    } else {
                        Toast.makeText(getActivity(), "Not verified", Toast.LENGTH_SHORT).show();

                        FirebaseAuth.getInstance().signOut();
                    }
                }
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode,resultCode,data);
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
            Toast.makeText(getActivity(),"sign in successfull",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getActivity(),"sign in cancle",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),ChooseLoginSignupActivity.class));
        }

        gotoMainActivity();

    }

    private void gotoMainActivity(){
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }

}





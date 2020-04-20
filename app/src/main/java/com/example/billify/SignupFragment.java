package com.example.billify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1001;

    ProgressDialog progressDialog;
    private EditText inputUserName,inputPhone,inputEmail,inputPassword;
    private Button btnSignIn,btnSignUp,btnResetPassword,btnGoogle,btnFaceboook;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private CallbackManager callbackManager;
    private SignInButton btnGoogleSignIn;
    private LoginButton btnFacebookLogIn;
    private CallbackManager callbackManager1;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private AccessToken accessToken;
    private GoogleApiClient googleApiClient;
    Billify bf;
    Friend f;
    private GoogleSignInOptions gso;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
         view = inflater.inflate(R.layout.activity_signup,container,false);

        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) view.findViewById(R.id.sign_in_button);
        btnSignUp = (Button) view.findViewById(R.id.sign_up_button);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.password);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnResetPassword = (Button) view.findViewById(R.id.btn_reset_password);
        inputUserName = (EditText) view.findViewById(R.id.username);
        inputPhone = (EditText) view.findViewById(R.id.phone);

        fragmentManager =getActivity().getSupportFragmentManager();

        btnFacebookLogIn = (LoginButton) view.findViewById(R.id.login_button);
        btnFaceboook = (Button) view.findViewById(R.id.fb);

        btnGoogleSignIn = (SignInButton) view.findViewById(R.id.google_button);
        btnGoogle = (Button) view.findViewById(R.id.google);



        callbackManager = CallbackManager.Factory.create();
        btnFacebookLogIn.setPermissions(Arrays.asList("email", "public_profile"));


        btnSignUp.setOnClickListener(this);
        // btnResetPassword.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);



        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),2, (GoogleApiClient.OnConnectionFailedListener) getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);      }
        });

        btnFaceboook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFacebookLogIn.performClick();
                btnFacebookLogIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        inputUserName.setText("success");
                        gotoMainActivity();

                    }

                    @Override
                    public void onCancel() {
                        inputUserName.setText("cancel");
                        gotoMainActivity();

                        Toast.makeText(getActivity(),"cancel",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        inputUserName.setText("error");
                        gotoMainActivity();

                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });



        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                new LoginFragment(),
                                "Login_Fragment").commit();
                break;

           /* case R.id.btn_reset_password:

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                new ResetPasswordFragment(),
                                "ForgotPassword_Fragment").commit();
                break;*/
            case R.id.sign_up_button:
                isValid(view);
                checkValidation();
                break;
        }
    }





    private void checkValidation()
    {
        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService (Context.CONNECTIVITY_SERVICE);
        // Get all edittext texts
        final String getFullName = inputUserName.getText().toString().trim();
        final String getEmailId = inputEmail.getText().toString().trim();
        final String getMobileNumber = inputPhone.getText().toString().trim();
        NetworkInfo activeNetworkInfo = conMgr.getActiveNetworkInfo();
        final String getPassword = inputPassword.getText().toString().trim();
        //String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile("[a-zA-Z0-9._-]+@[a" +
                "-z]+\\.+[a-z]+");

        Pattern p1 = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");


        Matcher m = p.matcher(getEmailId);
        Matcher m1 = p1.matcher(getPassword);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getPassword.equals("") || getPassword.length() == 0)
            //|| getConfirmPassword.equals("")
            //|| getConfirmPassword.length() == 0)

            Toast.makeText(getActivity(), "All fields are required.", Toast.LENGTH_SHORT)
                    .show();


            // Check if email id valid or not


        else if (!m1.find())
        {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())

                    .setMessage("Your password should must contain atleast one digit from 0-9, atleast one lowercase characters, atleast one uppercase characters, atleast one special symbol" +
                            "and length must be between size of 8 to 20")
                    .create();
            dialog.show();
        }

        // Check if both password should be equal
       /* else if (!getConfirmPassword.equals(getPassword))
            Toast.makeText(getActivity(), "Both password doesn't match.", Toast.LENGTH_SHORT)
                    .show();*/
        // Else do signup or do your stuff
        else if (conMgr == null || activeNetworkInfo == null)
        {



            AlertDialog dialog = new AlertDialog.Builder(getActivity())

                    .setMessage("Please check your connections")
                    .create();
            dialog.show();

        }
        else if( ((!getEmailId.equals("") || getEmailId.length() != 0)
                && (!getMobileNumber.equals("") || getMobileNumber.length() != 0)) || ((!getEmailId.equals("") || getEmailId.length() != 0)
                && (getMobileNumber.equals("") || getMobileNumber.length() == 0)))
        {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMax(100);
            progressDialog.setMessage("Its loading....");
            progressDialog.setTitle("Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();


            auth.createUserWithEmailAndPassword(getEmailId,getPassword)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                    {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            FirebaseUser currentperson = FirebaseAuth.getInstance().getCurrentUser();

                                            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(currentperson.getUid());

                                            ref.child("Name").setValue(getFullName);

                                            ref.child("Contact").setValue(getMobileNumber);

                                            ref.child("Email").setValue(getEmailId);
                                            ref.child("Balance").setValue("0");
                                            ref.child("Profile").setValue("");

                                            //ref.child("Password").setValue(getPassword);


                                            FirebaseAuth.getInstance().signOut();
                                            fragmentManager
                                                    .beginTransaction()
                                                    .replace(R.id.fragmentContainer,
                                                            new LoginFragment(),
                                                            "Login_Fragment").commit();
                                            Toast.makeText(getActivity(), "Registered Successfully.Please check your email for verification", Toast.LENGTH_SHORT)
                                                    .show();
                                            progressDialog.dismiss();

                                            getActivity().finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT)
                                                    .show();
                                        }


                                    }
                                });
//



                            }
                            else
                            {

                                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT)
                                        .show();
                                progressDialog.dismiss();
                            }





                        }



                    });
        }
        else if((getEmailId.equals("") || getEmailId.length() == 0)
                && (!getMobileNumber.equals("") || getMobileNumber.length() != 0))
        {
            Bundle bundle = new Bundle();
            bundle.putString("phone",getMobileNumber); // Put anything what you want
            bundle.putString("username",getFullName);
            VerifyPhoneFragment fragment2 = new VerifyPhoneFragment();
            fragment2.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment2)
                    .commit();
        }

    }






    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }



        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                loaduserprofile(currentAccessToken);
            }
        };


    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String userName = account.getDisplayName().toString();
            String userEmail = account.getEmail().toString();
            String userId = account.getId().toString();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

            f = new Friend();

            f = new Friend();
            f.setName(userName);
            f.setEmail(userEmail);
            f.setContact("");
            f.setProfile("");
            f.setBalance(0);
            f.setId(userId);

            bf.setYou(f);

            ref.child("Email").setValue(userEmail);
            ref.child("Contact").setValue("");
            ref.child("Balance").setValue("");
            ref.child("Profile").setValue("");
            ref.child("Name").setValue(userName);
            Toast.makeText(getActivity(),"sign in successfull",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getActivity(),"sign in cancle",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),ChooseLoginSignupActivity.class));
        }

        gotoMainActivity();

    }

    private void loaduserprofile(AccessToken newAccessToken){
        Toast.makeText(getActivity(),"load user profile",Toast.LENGTH_SHORT).show();
        gotoMainActivity();

        GraphRequest graphRequest = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                String userName = null;
                try {
                    userName = jsonObject.getString("first_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String userEmail = null;
                try {
                    userEmail = jsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String userId = null;
                try {
                    userId = jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                f = new Friend();

                f = new Friend();
                f.setName(userName);
                f.setEmail(userEmail);
                f.setContact("");
                f.setProfile("");
                f.setBalance(0);
                f.setId(userId);

                bf.setYou(f);

                ref.child("Email").setValue(userEmail);
                ref.child("Contact").setValue("");
                ref.child("Balance").setValue("");
                ref.child("Profile").setValue("");
                ref.child("Name").setValue(userName);
                Toast.makeText(getActivity(),"sign in successfull",Toast.LENGTH_SHORT).show();
            }



        });

        gotoMainActivity();

    }

    private void gotoMainActivity(){
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    private class Task1 extends AsyncTask<Void, Void, Void> {
        private View rootView;
        private View progressBar;
        private View btnLogin;

        public Task1 (View rootView){
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {
            progressBar = rootView.findViewById(R.id.login_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            btnLogin = rootView.findViewById(R.id.sign_in_button);
            btnLogin.setVisibility(View.GONE);
            // fadeAnimation(btnLogin, true);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
            btnLogin = rootView.findViewById(R.id.sign_in_button);
            btnLogin.setVisibility(View.VISIBLE);


            //CircularReveal circular = getInstance();

//            if (circular == null){
//                Toast.makeText(getContext(),"in",Toast.LENGTH_SHORT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Toast.makeText(getContext(),"in",Toast.LENGTH_SHORT);
//                    circular.expand();
//                }
//            }

            //fadeAnimation(btnLogin, false);
        }
    }


    private void shakeView(View view){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        view.startAnimation(animation);
    }


    private boolean isValid(View rootView){
//        EditText login = (EditText) rootView.findViewById(R.id.email);
//        EditText password = (EditText) rootView.findViewById(R.id.password);
//       EditText UserName = (EditText) rootView.findViewById(R.id.username);
//       EditText Phone = (EditText) rootView.findViewById(R.id.phone);

        if (inputUserName.getText().toString().isEmpty()){
            shakeView(inputUserName);
            return false;
        }if (inputPhone.getText().toString().isEmpty()){
            shakeView(inputPhone);
            return false;
        }
        if (inputEmail.getText().toString().isEmpty()){
            shakeView(inputEmail);
            return false;
        }
        if (inputPassword.getText().toString().isEmpty()){
            shakeView(inputPassword);
            return false;
        }

        return true;
    }


}

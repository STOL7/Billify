package com.example.loginapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;

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


        btnSignup.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnLogin.setOnClickListener(this);




        return view;
    }


    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.sign_in_button:
                checkValidation();
                break;

            case R.id.btn_reset_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                new ForgotPassword(),
                                "ForgotPassword_Fragment").commit();
                break;
            case R.id.sign_up_button:

                // Replace signup frgament with animation

                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new Register(),
                                "SignUp_Fragment").commit();
                break;
        }*/


    }
}

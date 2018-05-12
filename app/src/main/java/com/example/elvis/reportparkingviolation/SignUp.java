package com.example.elvis.reportparkingviolation;

/**
 * Elvis Gu, May 2018
 */
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUp extends AppCompatActivity {
    private EditText mEditTextSignUpUsername;
    private EditText mEditTextSignUpPassword;

    private TextView mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mBtnSignUp = (TextView) findViewById(R.id.main_btn_signin);
        mBtnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextSignUpUsername = (EditText) findViewById(R.id.username_animation_login);
                mEditTextSignUpPassword = (EditText) findViewById(R.id.password_animation_login);
                appUser bu = new appUser();
                bu.setUsername(mEditTextSignUpUsername.getText().toString());
                bu.setPassword(mEditTextSignUpPassword.getText().toString());
                bu.setPolice(false);
                bu.signUp(new SaveListener<appUser>() {
                    @Override
                    public void done(appUser appUser, BmobException e) {
                        if(e==null){
                            Toast.makeText(SignUp.this,"注册成功："+ appUser.getUsername(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}

package com.example.elvis.reportparkingviolation;
/**
 * Elvis Gu, May 2018
 */
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.bmob.v3.BmobUser;
/**
 * a splash animation when open the application.
 * */

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                User user = BmobUser.getCurrentUser(User.class);
//                if (user == null) {
//                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
//                    finish();
//                }else{
//                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                    finish();
//                }
                startActivity(new Intent(splash.this,AnimatLogin.class));
                    finish();
            }
        },2000);

    }
}

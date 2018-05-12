package com.example.elvis.reportparkingviolation;

/**
 * Elvis Gu, May 2018
 * The main activity to make sure Bmob api and Baidu map api could used in all activities.
 */

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;

public class MapApplication extends Application {
    public void onCreate(){
        super.onCreate();
        SDKInitializer.initialize(this);
        Bmob.initialize(this, "f732e2fc958d8b5499561c171d8ca72d");
    }
}

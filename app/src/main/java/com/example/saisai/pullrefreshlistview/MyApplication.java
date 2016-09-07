package com.example.saisai.pullrefreshlistview;

import android.app.Application;

/**
 * Created by saisai on 2016/8/22.
 */
public class MyApplication extends Application {

    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }

}

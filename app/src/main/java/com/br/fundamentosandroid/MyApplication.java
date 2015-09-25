package com.br.fundamentosandroid;

import android.app.Application;

import com.br.fundamentosandroid.util.AppUtil;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.CONTEXT_APPLICATION = getApplicationContext();
    }
}
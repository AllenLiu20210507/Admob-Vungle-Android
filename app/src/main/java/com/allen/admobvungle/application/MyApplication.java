package com.allen.admobvungle.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MyApplication extends Application {
    private static final String TAG ="allen"+ Application.class.getSimpleName();
    private static final String SHARE_APP_TAG ="allen_first_start";
    public  AppOpenManager appOpenManager;
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });
        appOpenManager=AppOpenManager.getAppOpenManager(this);
        appOpenManager.fetchAd();
    }
}

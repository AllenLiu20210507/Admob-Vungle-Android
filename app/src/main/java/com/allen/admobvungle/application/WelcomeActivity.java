package com.allen.admobvungle.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.allen.admobvungle.MainActivity;
import com.allen.admobvungle.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity  {
    public  AppOpenManager appOpenManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        appOpenManager=AppOpenManager.getAppOpenManager(this);
        appOpenManager.setFirstStartListner(firstStartListner);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public FirstStartListner firstStartListner=new FirstStartListner() {
      @Override
      public void onAppFisrtAdLoaded() {
         appOpenManager.showAdIfAvailable(WelcomeActivity.this);
      }

     @Override
     public void onAppFirstClosed() {
         Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
         startActivity(intent);
         finish();

     }
 };
}

package com.allen.admobvungle.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.allen.admobvungle.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity  {
    public  AppOpenManager appOpenManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appOpenManager=AppOpenManager.getAppOpenManager(this);
        appOpenManager.setFirstStartListner(firstStartListner);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000,0);
    }

 public FirstStartListner firstStartListner=new FirstStartListner() {
      @Override
      public void onAppFisrtAdLoaded() {
         appOpenManager.showAdIfAvailable(WelcomeActivity.this);
      }
  };
}

package com.allen.admobvungle.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.allen.admobvungle.MainActivity;
import com.allen.admobvungle.R;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends Activity {
    private static final String TAG = "allen" + WelcomeActivity.class.getSimpleName();

    public AppOpenManager appOpenManager;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //long delayMillis)
            //从当前时间开始延迟delayMillis时间后执行Runnable
            log("runnable run");
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        appOpenManager = AppOpenManager.getAppOpenManager(this);
        appOpenManager.setFirstStartListner(firstStartListner);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public FirstStartListner firstStartListner = new FirstStartListner() {
        @Override
        public void onAppFisrtAdLoaded() {
            log("onAppFisrtAdLoaded");

//            handler.postDelayed(runnable, 5000);
//            Toast.makeText(WelcomeActivity.this, "Simulate Ad 5 seconds countdown  ", Toast.LENGTH_LONG).show();
            appOpenManager.showAdIfAvailable(WelcomeActivity.this);
        }

        @Override
        public void onAppFirstClosed() {
            log("onAppFirstClosed");
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    };

    // OTHERS  log
    private void log(CharSequence text) {
        Log.d(TAG, text.toString());

    }
}

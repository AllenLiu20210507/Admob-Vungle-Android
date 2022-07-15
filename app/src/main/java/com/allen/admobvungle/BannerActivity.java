package com.allen.admobvungle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;


public class BannerActivity extends Activity {
    private AdView mAdViewBanner,mAdViewMrec;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mAdViewBanner = findViewById(R.id.adView1);
        mAdViewMrec = findViewById(R.id.adView2);

        //Banner init
        mAdViewBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                log("onAdLoaded");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                log("onAdClicked");

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                log("onAdImpression");

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                log("onAdClosed");

            }

            @Override
            public void onAdFailedToLoad(@NonNull  LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                log("loadAdError"+loadAdError);

            }
        });

        //Mrec init

        mAdViewMrec.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                log("onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull  LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                log("loadAdError"+loadAdError);

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                log("onAdClicked");

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                log("onAdImpression");

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                log("onAdClosed");

            }
        });
    }


    public void loadBanner(View view) {
        AdRequest adRequest = new AdRequest.Builder().build();
//        RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("6A771E54DF8B42314F02EE15E821BC18")
        mAdViewBanner.loadAd(adRequest);
    }
    public void loadMREC(View view) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewMrec.loadAd(adRequest);
    }
    // OTHERS  log
    private void log(CharSequence text) {
        Log.d(TAG, text.toString());

    }

}

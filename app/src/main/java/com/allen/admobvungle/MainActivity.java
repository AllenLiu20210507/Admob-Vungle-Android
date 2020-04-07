package com.allen.admobvungle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    private static  String mAdUnitIdRewardBased = "";
    private static  String mAdUnitIdInterstitial = "";


    private Button showRewardedVideoAdButton;
    private Button showInterstitialAdButton;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdUnitIdRewardBased=getString(R.string.reward_placement_id);
        mAdUnitIdInterstitial=getString(R.string.interstitial_placement_id);
        //init components
        showRewardedVideoAdButton = ((Button) this.findViewById(R.id.show));
        showInterstitialAdButton = ((Button) this.findViewById(R.id.showInterstitial));
        mAdView = findViewById(R.id.adView);

        //RewardedVideoAd init
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(mRewardedVideoAdListener);

        //InterstitialAd init
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(mAdUnitIdInterstitial);
        mInterstitialAd.setAdListener(mAdListener);


        //Banner init
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                log("onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                log("onAdFailedToLoad"+"   errorCode"+errorCode);

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
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                log("onAdClosed");

            }
        });

        //init MobileAds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                log(initializationStatus.toString() + "init successfully");
            }
        });
    }




    public void loadRewardedVideo(View view) {

        mRewardedVideoAd.loadAd(mAdUnitIdRewardBased, new AdRequest.Builder().build());
    }

    public void showRewardedVideo(View view) {
        log("Showing reward-based ad…");
        mRewardedVideoAd.show();
    }

    public void loadInterstitial(View view) {

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void showInterstitial(View view) {
        log("Showing interstitial ad…");
        mInterstitialAd.show();
    }

    public void loadBanner(View view) {
//        AdSize adSize=new AdSize(300,50);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("8B4CFB7EDE2B10EF62C16A83B4B20987").build();
        mAdView.loadAd(adRequest);
    }


    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdClosed() {
            log("onInterstitialAdClosed");
//            showInterstitialAdButton.setEnabled(false);
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            log("onInterstitialAdFailedToLoad: error " + errorCode);
//            showInterstitialAdButton.setEnabled(false);
        }

        @Override
        public void onAdLeftApplication() {
            log("onInterstitialAdLeftApplication");
        }

        @Override
        public void onAdLoaded() {
            log("onInterstitialAdLoaded");
            showInterstitialAdButton.setEnabled(true);
        }

        @Override
        public void onAdOpened() {
            log("onInterstitialAdOpened");
        }
    };

    private RewardedVideoAdListener mRewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLeftApplication() {
            log("onRewardedVideoAdLeftApplication");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            log("onRewardedVideoAdClosed");
            showRewardedVideoAdButton.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            log("onRewardedVideoAdFailedToLoad: error " + errorCode);
            showRewardedVideoAdButton.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdLoaded() {
            log("onRewardedVideoAdLoaded");
            showRewardedVideoAdButton.setEnabled(true);

        }

        @Override
        public void onRewardedVideoAdOpened() {
            log("onRewardedVideoAdOpened");
        }

        @Override
        public void onRewarded(RewardItem reward) {
            log("onRewarded! currency: " + reward.getType() + ", amount: " + reward.getAmount());
        }

        @Override
        public void onRewardedVideoStarted() {
            log("onRewardedVideoStarted");
        }

        @Override
        public void onRewardedVideoCompleted() {

        }
    };


    // OTHERS  log
    private void log(CharSequence text) {
        Log.d(TAG, text.toString());

    }

}

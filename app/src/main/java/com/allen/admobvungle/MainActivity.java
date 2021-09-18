package com.allen.admobvungle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RewardedAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    private static  String mAdUnitIdRewardBased = "";
    private static  String mAdUnitIdInterstitial = "";


    private Button showRewardedVideoAdButton;
    private Button showInterstitialAdButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdUnitIdRewardBased=getString(R.string.reward_placement_id);
        mAdUnitIdInterstitial=getString(R.string.interstitial_placement_id);
        //init components
        showRewardedVideoAdButton = ((Button) this.findViewById(R.id.show));
        showInterstitialAdButton = ((Button) this.findViewById(R.id.showInterstitial));










        //init MobileAds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                log(initializationStatus.toString() + "init successfully");
            }
        });
    }




    public void loadRewardedVideo(View view) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, mAdUnitIdRewardBased,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedVideoAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedVideoAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
        mRewardedVideoAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                mRewardedVideoAd = null;
            }
        });
    }

    public void showRewardedVideo(View view) {
        log("Showing reward-based ad…");

        if (mRewardedVideoAd != null) {
            Activity activityContext = MainActivity.this;
            mRewardedVideoAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }

    }

    public void loadInterstitial(View view) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,mAdUnitIdInterstitial, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mInterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });
    }

    public void showInterstitial(View view) {
        log("Showing interstitial ad…");
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    public void goToBannerAndMrec(View view) {
        Intent intent=new Intent(MainActivity.this,BannerActivity.class);
        startActivity(intent);
    }





    // OTHERS  log
    private void log(CharSequence text) {
        Log.d(TAG, text.toString());

    }

}

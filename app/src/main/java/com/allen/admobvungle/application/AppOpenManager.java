package com.allen.admobvungle.application;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.allen.admobvungle.MainActivity;
import com.allen.admobvungle.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

/**
 * Prefetches App Open Ads.
 */
public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String TAG = "allen" + AppOpenManager.class.getSimpleName();

    /**
     * 是否是冷启动
     */
    public  boolean isColdLaunch=false;

    private static final String LOG_TAG = "AppOpenManager";
    private final String AD_UNIT_ID;
    private AppOpenAd appOpenAd = null;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private FirstStartListner firstStartListner;

    private final Context mContext;

    private static  AppOpenManager appOpenManager = null;

    // 私有的构造方法
    private AppOpenManager(Context context) {
        this.mContext = context;
        AD_UNIT_ID = mContext.getString(R.string.open_ad_id);
        Application application =(Application)mContext;//强制转换
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    // 以自己实例为返回值的静态的公有方法，静态工厂方法
    public static AppOpenManager getAppOpenManager(Context context) {
        if (appOpenManager==null){
            appOpenManager=new AppOpenManager(context);
        }
        return appOpenManager;
    }

        /**
         * Constructor
         */



    public void setFirstStartListner(FirstStartListner firstStartListner) {
        this.firstStartListner = firstStartListner;

    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        // We will implement this below.
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAppOpenAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        if (firstStartListner != null&&isColdLaunch) {
                            firstStartListner.onAppFisrtAdLoaded();
                        }
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                        log("onAppOpenAdFailedToLoad      " + loadAdError.toString());
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                mContext, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }


    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null;
    }


    private static boolean isShowingAd = false;

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable(Activity currentactivity) {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        log("isShowingAd"+isShowingAd+"isAdAvailable"+isAdAvailable());
        if (!isShowingAd && isAdAvailable()) {

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            log("onAdDismissedFullScreenContent");
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                            if (firstStartListner != null) {
                                firstStartListner.onAppFirstClosed();
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            log(adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.show(currentactivity, fullScreenContentCallback);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }


    // OTHERS  log
    private void log(CharSequence text) {
        Log.d(TAG, text.toString());

    }

    private int mFinalCount=0;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        mFinalCount++;
        //如果mFinalCount ==1，说明冷启动，第一次打开activity
        log("onActivityStarted" + activity.toString()+"mFinalCount"+mFinalCount);
        if (mFinalCount == 1) {
            isColdLaunch=true;
        }else{
            isColdLaunch=false;
            showAdIfAvailable(activity);
        }
        log("onActivityStarted" + activity.toString());

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {


    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
//        showAdIfAvailable();
//        log("onStart");
    }
}
package com.allen.admobvungle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NativeAdActivity extends Activity {
    private static final String TAG = NativeAdActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private NativeAdAdapter adapter;
    private Button loadAdButton;
    private final List<Object> items = new ArrayList<>();
    private final List<NativeAd> nativeAds = new ArrayList<>();
    private String nativeAdUnitId;
    
    // 广告占位符对象，用于标识需要加载广告的位置
    private static final Object AD_PLACEHOLDER = new Object();
    
    // 记录正在加载的位置，避免重复加载
    private final Set<Integer> loadingPositions = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);

        nativeAdUnitId = getString(R.string.native_ad_unit_id);

        initViews();
        setupRecyclerView();
        loadFeedData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        loadAdButton = findViewById(R.id.loadNativeAdButton);
        // 隐藏按钮，因为改为懒加载
        loadAdButton.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NativeAdAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void loadFeedData() {
        // 第一个位置就是广告占位符
        items.add(AD_PLACEHOLDER);
        
        // 添加50个示例内容项，每10个内容项后插入一个广告占位符
        // 广告位置：0, 11, 22, 33, 44 (第一个是0，之后每11个位置一个广告)
        int contentIndex = 1;
        for (int i = 1; i <= 50; i++) {
            items.add("内容项 " + contentIndex);
            contentIndex++;
            
            // 每10个内容项后插入一个广告占位符
            if (i % 10 == 0 && i < 50) {
                items.add(AD_PLACEHOLDER);
            }
        }
        adapter.notifyDataSetChanged();
        
        // 一进入页面就加载第一个广告（位置0）
        loadNativeAdForPosition(0);
    }

    /**
     * 为指定位置加载Native广告（懒加载）
     * @param position 广告占位符在列表中的位置
     */
    private void loadNativeAdForPosition(final int position) {
        if (nativeAdUnitId == null || nativeAdUnitId.isEmpty() || nativeAdUnitId.equals("Your_native_ad_unit_id")) {
            log("请配置Native Ad Unit ID");
            return;
        }

        // 检查位置是否有效，以及该位置是否还是占位符
        if (position < 0 || position >= items.size() || items.get(position) != AD_PLACEHOLDER) {
            log("Position " + position + " is not a valid ad placeholder");
            return;
        }

        // 检查是否正在加载，避免重复加载
        if (loadingPositions.contains(position)) {
            log("Position " + position + " is already loading");
            return;
        }

        loadingPositions.add(position);
        log("Loading native ad for position: " + position);

        AdLoader adLoader = new AdLoader.Builder(this, nativeAdUnitId)
                .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        log("Native ad loaded successfully for position: " + position);
                        nativeAds.add(nativeAd);
                        loadingPositions.remove(position);
                        
                        // 在主线程中替换占位符
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (position < items.size() && items.get(position) == AD_PLACEHOLDER) {
                                    items.set(position, nativeAd);
                                    adapter.notifyItemChanged(position);
                                } else {
                                    // 如果位置已经改变，销毁广告
                                    nativeAd.destroy();
                                }
                            }
                        });
                    }
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        log("Native ad failed to load for position " + position + ": " + loadAdError.getMessage());
                        loadingPositions.remove(position);
                        // 加载失败时，可以选择移除占位符或保留
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 显示 Toast 提示
                                Toast.makeText(NativeAdActivity.this, 
                                        "Native广告加载失败: " + loadAdError.getMessage(), 
                                        Toast.LENGTH_LONG).show();
                                
                                if (position < items.size() && items.get(position) == AD_PLACEHOLDER) {
                                    // 移除占位符，显示普通内容
                                    items.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                            }
                        });
                    }

                    @Override
                    public void onAdClicked() {
                        log("Native ad clicked");
                    }

                    @Override
                    public void onAdImpression() {
                        log("Native ad impression recorded");
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                        .build())
                .build();

        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        // 销毁所有Native广告
        if (nativeAds != null) {
            for (NativeAd ad : nativeAds) {
                if (ad != null) {
                    ad.destroy();
                }
            }
            nativeAds.clear();
        }
        super.onDestroy();
    }

    private void log(CharSequence text) {
        Log.d(TAG, text.toString());
    }

    // Adapter 作为内部类
    private class NativeAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_CONTENT = 0;
        private static final int TYPE_AD = 1;
        private static final int TYPE_AD_PLACEHOLDER = 2;

        @Override
        public int getItemViewType(int position) {
            Object item = items.get(position);
            if (item instanceof NativeAd) {
                return TYPE_AD;
            } else if (item == AD_PLACEHOLDER) {
                return TYPE_AD_PLACEHOLDER;
            } else {
                return TYPE_CONTENT;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
            if (viewType == TYPE_AD) {
                View view = inflater.inflate(R.layout.native_ad_template, parent, false);
                return new NativeAdHolder(view);
            } else if (viewType == TYPE_AD_PLACEHOLDER) {
                // 占位符使用普通内容布局，显示加载中
                View view = inflater.inflate(R.layout.item_feed_content, parent, false);
                return new AdPlaceholderHolder(view);
            } else {
                View view = inflater.inflate(R.layout.item_feed_content, parent, false);
                return new ContentHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ContentHolder) {
                Log.d(TAG,"ContentHolder"+position);
                ((ContentHolder) holder).bind((String) items.get(position));
            } else if (holder instanceof NativeAdHolder) {
                Log.d(TAG,"NativeAdHolder"+position);
                ((NativeAdHolder) holder).bind((NativeAd) items.get(position));
            } else if (holder instanceof AdPlaceholderHolder) {
                Log.d(TAG,"AdPlaceholderHolder"+position);
                // 当绑定占位符时，触发加载广告
                ((AdPlaceholderHolder) holder).bind(position);
                loadNativeAdForPosition(position);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class ContentHolder extends RecyclerView.ViewHolder {
        android.widget.TextView textView;

        ContentHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
        }

        void bind(String text) {
            textView.setText(text);
        }
    }

    // 广告占位符ViewHolder，显示加载中状态
    private static class AdPlaceholderHolder extends RecyclerView.ViewHolder {
        android.widget.TextView textView;

        AdPlaceholderHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
        }

        void bind(int position) {
            textView.setText("广告加载中... (位置: " + position + ")");
        }
    }

    private static class NativeAdHolder extends RecyclerView.ViewHolder {
        com.google.android.gms.ads.nativead.NativeAdView adView;

        NativeAdHolder(@NonNull View itemView) {
            super(itemView);
            adView = (com.google.android.gms.ads.nativead.NativeAdView) itemView;
            // 核心：设置不可回收，确保一次创建，一次绑定，一直用
            setIsRecyclable(false);
        }

        void bind(com.google.android.gms.ads.nativead.NativeAd nativeAd) {
            // 核心思想：一次创建，一次绑定，一直用
            // 由于设置了 setIsRecyclable(false)，这个 ViewHolder 不会被回收重用
            
            // 检查是否已经绑定过（通过检查视图引用是否已设置）
            boolean isAlreadyBound = (adView.getMediaView() != null);
            
            if (!isAlreadyBound) {
                // 只在第一次绑定时设置所有视图引用
                // Set the media view
                adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));

                // Set other ad assets (只设置布局文件中存在的视图)
                adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
                adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));

                // The headline and mediaContent are guaranteed to be in every NativeAd
                ((android.widget.TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
                adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
                
                // Set call to action if available
                if (nativeAd.getCallToAction() != null) {
                    ((android.widget.TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
                }
                
                Log.d(TAG, "bind NativeAd: First time binding, mediaContent=" + (nativeAd.getMediaContent() != null ? "not null, hasVideo=" + nativeAd.getMediaContent().hasVideoContent() : "null") + ", nativeAd hashCode=" + nativeAd.hashCode());
            } else {
                // 如果已经绑定过，只需要重新设置 NativeAd 以恢复点击功能
                Log.d(TAG, "bind NativeAd: Re-binding same ad to restore click, nativeAd hashCode=" + nativeAd.hashCode());
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad. The SDK will handle the rest.
            adView.setNativeAd(nativeAd);
        }
    }
}

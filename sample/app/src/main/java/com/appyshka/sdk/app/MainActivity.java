package com.appyshka.sdk.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.appyshka.sdk.APController;
import com.appyshka.sdk.APManager;
import com.appyshka.sdk.APView;
import com.appyshka.sdk.IAPAdListener;

public class MainActivity extends Activity {
    private APManager mAdManager;   // Appyshka Ads Manager instance
    private Toast mToast;           // currently displayed toast
    private FrameLayout mAdFrame;   // frame used for offer displaying
    private APView mOfferView;      // offer view

    private final static String APSDK_APP_ID = "INSERT YOUR APP ID HERE";

    private static class APAdListener implements IAPAdListener {
        protected final String logTag;

        public APAdListener(final String logTag) {
            this.logTag = logTag;
        }

        @Override
        public void onNoAdFound() {
            Log.d(logTag, "onNoAdFound");
        }

        @Override
        public void onAdLoaded() {
            Log.d(logTag, "onAdLoaded");
        }

        @Override
        public void onAdShown() {
            Log.d(logTag, "onAdShown");
        }

        @Override
        public void onAdClicked() {
            Log.d(logTag, "onAdClicked");
        }

        @Override
        public void onAdClosed() {
            Log.d(logTag, "onAdClosed");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdManager = APManager.getInstance(this);
        mAdFrame = (FrameLayout) findViewById(R.id.ap_banner_content);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdManager.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdManager.onPause(this);
        if(mToast != null)
            mToast.cancel();
    }

    @Override
    public void onDestroy() {
        mAdManager.release(this);
        if(mToast != null)
            mToast.cancel();
        super.onDestroy();
    }

    public void showBannerList(View v) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            removeBanner();

            APController c = new APController(APController.TYPE_BANNER_LIST);
            c.setAdsCount(2);

            mOfferView = APManager.getInstance(this).getSingleBannerView(c, APSDK_APP_ID);
            mOfferView.setAdListener(
                    new APAdListener("APBannerListView") {
                        @Override
                        public void onNoAdFound() {
                            super.onNoAdFound();
                            showToast(getString(R.string.connection_error));
                        }
                    }
            );
            mOfferView.loadAd();

            mAdFrame.addView(mOfferView);
        } else {
            // landscape, view may overlap with buttons, do not show anything
            showToast(getString(R.string.not_enought_space_error));
        }
    }

    public void showBannerView(View v) {
        removeBanner();

        APController c = new APController(APController.TYPE_SINGLE_BANNER);

        mOfferView = APManager.getInstance(this).getSingleBannerView(c, APSDK_APP_ID);
        mOfferView.setAdListener(
                new APAdListener("APSingleBannerView") {
                    @Override
                    public void onNoAdFound() {
                        super.onNoAdFound();
                        showToast(getString(R.string.connection_error));
                    }
                }
        );
        mOfferView.loadAd();

        mAdFrame.addView(mOfferView);
    }

    public void loadBannerWall(View v) {
        APManager.getInstance(this).initBannerWall(APSDK_APP_ID, new APAdListener("APSampleBannerWall") {
            @Override
            public void onNoAdFound() {
                super.onNoAdFound();
                showToast(getString(R.string.connection_error));
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                showToast(getString(R.string.banner_wall_ad_loaded));
            }
        });
    }

    public void showBannerWall(View v) {
        removeBanner();
        APManager.getInstance(this).showBannerWall(APSDK_APP_ID, new APAdListener("APSampleBannerWall") {
            @Override
            public void onNoAdFound() {
                super.onNoAdFound();
                showToast(getString(R.string.connection_error));
            }
        });
    }

    private void removeBanner() {
        if (mOfferView != null) {
            mAdFrame.removeView(mOfferView);
            mOfferView.releaseAd();
            mOfferView = null;
        }
    }

    private void showToast(String message) {
        if(mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(this, message,
                Toast.LENGTH_SHORT);
        mToast.show();
    }
}

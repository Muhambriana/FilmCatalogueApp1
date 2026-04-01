package com.muhamapps.filmcatalogueapp1.ads

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.muhamapps.filmcatalogueapp1.BuildConfig

class AdsManager {

    fun loadBanner(context: Context, container: FrameLayout) {
        val adRequest = AdRequest.Builder().build()
        val adView = AdView(context).apply {
            setAdSize(AdSize.getInlineAdaptiveBannerAdSize(AdSize.FULL_WIDTH, 60))
            adUnitId = BuildConfig.ADMOB_BANNER_ID
            loadAd(adRequest)
        }

        container.removeAllViews()
        container.addView(adView)
    }

    fun loadInterstitial(activity: Activity, callback: InterstitialAdLoadCallback) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, BuildConfig.ADMOB_INTERSTITIAL_ID, adRequest, callback)
    }
}
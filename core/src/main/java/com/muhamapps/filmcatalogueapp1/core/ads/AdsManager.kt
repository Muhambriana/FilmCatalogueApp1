package com.muhamapps.filmcatalogueapp1.core.ads

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
//import com.muhamapps.filmcatalogueapp1.BuildConfig

class AdsManager {

    private val adPool = mutableListOf<NativeAd>()
    private var isLoading = false

    fun loadBanner(context: Context, container: FrameLayout) {
        val adRequest = AdRequest.Builder().build()
        val adView = AdView(context).apply {
            setAdSize(AdSize.getInlineAdaptiveBannerAdSize(AdSize.FULL_WIDTH, 300))
            adUnitId = "BuildConfig.ADMOB_BANNER_ID"
            loadAd(adRequest)
        }

        container.removeAllViews()
        container.addView(adView)
    }

    fun loadInterstitial(activity: Activity, callback: InterstitialAdLoadCallback) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, "BuildConfig.ADMOB_INTERSTITIAL_ID", adRequest, callback)
    }

    fun preLoads(context: Context, count: Int = 3) {
        if (isLoading || adPool.size >= count) return

        isLoading = true

        val adLoader = AdLoader.Builder(context, "BuildConfig.ADMOB_BANNER_ID")
            .forNativeAd { ad ->
                adPool.add(ad)
                isLoading = false
            }
            .build()

        repeat(count) {
            val adRequest = AdRequest.Builder().build()
            adLoader.loadAd(adRequest)
        }
    }

    fun getAd(): NativeAd? {
        return if (adPool.isNotEmpty()) {
            adPool.removeAt(0)
        } else null
    }

    fun destroy() {
        adPool.forEach { it.destroy() }
        adPool.clear()
    }
}
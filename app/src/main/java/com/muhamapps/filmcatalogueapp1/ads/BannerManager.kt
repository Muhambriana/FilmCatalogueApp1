package com.muhamapps.filmcatalogueapp1.ads

import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.muhamapps.filmcatalogueapp1.BuildConfig

class BannerManager {

    fun loadBanner(context: Context, container: FrameLayout) {
        val adView = AdView(context).apply {
            setAdSize(AdSize.getInlineAdaptiveBannerAdSize(AdSize.FULL_WIDTH, 60))
            adUnitId = BuildConfig.ADMOB_BANNER_ID
            loadAd(AdRequest.Builder().build())
        }

        container.removeAllViews()
        container.addView(adView)
    }
}
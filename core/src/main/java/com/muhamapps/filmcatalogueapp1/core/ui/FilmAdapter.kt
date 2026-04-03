package com.muhamapps.filmcatalogueapp1.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.nativead.NativeAd
import com.muhamapps.filmcatalogueapp1.core.R
import com.muhamapps.filmcatalogueapp1.core.databinding.ItemListAdBinding
import com.muhamapps.filmcatalogueapp1.core.databinding.ItemListFilmBinding
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.domain.model.GridItem
import com.muhamapps.filmcatalogueapp1.core.utils.NetworkInfo.IMAGE_URL
import androidx.core.view.isNotEmpty
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView
import com.muhamapps.filmcatalogueapp1.core.ads.AdsManager
import com.muhamapps.filmcatalogueapp1.core.utils.Config

class FilmAdapter(
    private val callback: FilmShareCallback,
    private val adsManager: AdsManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_CONTENT = 1
        const val VIEW_TYPE_AD = 2
    }

    private var listData = ArrayList<GridItem>()
    var onItemClick: ((Film) -> Unit)? = null

    fun setData(newListData: List<GridItem.Content>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(insertAds(newListData))
        notifyDataSetChanged()
    }

    private fun insertAds(list: List<GridItem.Content>): List<GridItem> {
        val result = mutableListOf<GridItem>()

        list.forEachIndexed { idx, item ->
            val normalIndex = idx + 1
            result.add(item)

            val isInsertAd = ((normalIndex / Config.TOTAL_ITEM_PER_AD) > 0) && (normalIndex % Config.TOTAL_ITEM_PER_AD == 0)
            if (isInsertAd || idx == list.lastIndex) {
                result.add(GridItem.Ad)
            }
        }

        return result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CONTENT -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_list_film,
                    parent,
                    false
                )
                ContentViewHolder(view)
            }
            VIEW_TYPE_AD -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_list_ad,
                    parent,
                    false
                )
                AdViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(val item = listData[position]) {
            is GridItem.Content -> (holder as ContentViewHolder).bind(item)
            is GridItem.Ad -> (holder as AdViewHolder).bind()
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when(listData[position]) {
            is GridItem.Content -> VIEW_TYPE_CONTENT
            is GridItem.Ad -> VIEW_TYPE_AD
        }
    }

    override fun getItemCount() = listData.size

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListFilmBinding.bind(itemView)

        fun bind(data: GridItem.Content) {
            val film = data.film

            with(binding) {
                imgShare.setOnClickListener { callback.onShareClick(film) }
                Glide.with(itemView.context)
                    .load(IMAGE_URL + film.poster)
                    .into(ivItemImage)
                tvItemTitle.text = film.title
                tvItemSubtitle.text = film.rating
            }
        }

        init {
            binding.root.setOnClickListener {
                val gridItem = listData[bindingAdapterPosition] as? GridItem.Content
                gridItem?.film?.let {
                    onItemClick?.invoke(it)
                }
            }
        }
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListAdBinding.bind(itemView)
        private var currentAd: NativeAd? = null

        fun bind() {
            if (binding.root.isNotEmpty()) return

            val ad = adsManager.getAd() ?: return
            currentAd = ad

            val adView = LayoutInflater.from(binding.root.context).inflate(
                R.layout.native_ad_layout,
                binding.root,
                false
            ) as NativeAdView

            populate(ad, adView)

            binding.root.removeAllViews()
            binding.root.addView(adView)
        }

        private fun populate(ad: NativeAd, adView: NativeAdView) {
            val context = adView.context

            // Find view references matching the XML layout IDs
            val headline = adView.findViewById<TextView>(R.id.ad_headline)
            val body = adView.findViewById<TextView?>(R.id.ad_body)
            val cta = adView.findViewById<TextView?>(R.id.ad_call_to_action)
            val iconView = adView.findViewById<ImageView?>(R.id.ad_app_icon)
            val mediaView = adView.findViewById<MediaView?>(R.id.ad_media)

            // Headline (required)
            adView.headlineView = headline
            headline.text = ad.headline

            // Body (optional)
            if (ad.body != null && body != null) {
                adView.bodyView = body
                body.text = ad.body
                body.visibility = View.VISIBLE
            } else {
                body?.visibility = View.GONE
            }

            // Call to action (optional)
            if (ad.callToAction != null && cta != null) {
                adView.callToActionView = cta
                cta.text = ad.callToAction
                cta.visibility = View.VISIBLE
            } else {
                cta?.visibility = View.GONE
            }

            // Icon (optional) — uses ad_app_icon from layout
            if (ad.icon != null && iconView != null) {
                adView.iconView = iconView
                val iconUri = ad.icon?.uri
                if (iconUri != null) {
                    Glide.with(context).load(iconUri).into(iconView)
                    iconView.visibility = View.VISIBLE
                } else {
                    iconView.visibility = View.GONE
                }
            } else {
                iconView?.visibility = View.GONE
            }

            // Media content (optional)
            if (ad.mediaContent != null && mediaView != null) {
                adView.mediaView = mediaView
                mediaView.visibility = View.VISIBLE
            } else {
                mediaView?.visibility = View.GONE
            }

            // Attach the native ad to the view
            adView.setNativeAd(ad)
        }

    }
}
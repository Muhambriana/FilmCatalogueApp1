package com.muhamapps.filmcatalogueapp1.home

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.muhamapps.filmcatalogueapp1.R
import com.muhamapps.filmcatalogueapp1.core.data.Resource
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.ui.FilmAdapter
import com.muhamapps.filmcatalogueapp1.core.ui.FilmShareCallback
import com.muhamapps.filmcatalogueapp1.databinding.ActivityHomeBinding
import com.muhamapps.filmcatalogueapp1.detail.DetailFilmActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.muhamapps.filmcatalogueapp1.core.ads.AdsManager
import com.muhamapps.filmcatalogueapp1.core.domain.model.GridItem
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity(), FilmShareCallback {

    private val homeViewModel: HomeViewModel by viewModel()
    private val adsManager: AdsManager by inject()

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Bmdb"

        val callback = object: InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitialAd.show(this@HomeActivity)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {

            }
        }

        adsManager.loadInterstitial(this, callback)

        getFilmData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                val uriFavorite = "filmcatalogueapp1://favorite".toUri()
                startActivity(Intent(Intent.ACTION_VIEW, uriFavorite))
                true
            }
            else -> true
        }
    }

    override fun onShareClick(data: Film) {
        val mimeType = "text/plain"
        @Suppress("DEPRECATION")
        ShareCompat.IntentBuilder
            .from(this)
            .setType(mimeType)
            .setChooserTitle("Bagikan aplikasi ini sekarang.")
            .setText("Lihat Film ${data.title} di themoviedb.org")
            .startChooser()
    }

    private fun getFilmData() {
        val filmAdapter = FilmAdapter(this, adsManager)
        filmAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailFilmActivity::class.java)
            intent.putExtra(DetailFilmActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        homeViewModel.film.observe(this, { film ->
            if (film != null) {
                when (film) {
                    is Resource.Loading -> binding?.progressBar?.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        adsManager.preLoads(this, 3)
                        filmAdapter.setData(film.data as List<GridItem.Content>)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        })


        val layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (filmAdapter.getItemViewType(position) == FilmAdapter.VIEW_TYPE_AD) 2
                    else 1
                }
            }
        }

        with(binding?.rvFilm) {
            this?.layoutManager = layoutManager
            this?.setHasFixedSize(true)
            this?.adapter = filmAdapter
        }
    }

}
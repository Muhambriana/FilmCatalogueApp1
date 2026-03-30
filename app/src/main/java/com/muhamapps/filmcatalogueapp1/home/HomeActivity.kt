package com.muhamapps.filmcatalogueapp1.home

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
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
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.muhamapps.filmcatalogueapp1.BuildConfig
import com.muhamapps.filmcatalogueapp1.utils.BannerManager

class HomeActivity : AppCompatActivity(), FilmShareCallback {

    private val homeViewModel: HomeViewModel by viewModel()

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

        MobileAds.initialize(this)
        BannerManager.loadBanner(this, binding.adViewContainer)

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
        val filmAdapter = FilmAdapter(this)
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
                        filmAdapter.setData(film.data)
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        })

        with(binding?.rvFilm) {
            this?.layoutManager =
                if (this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(context,2)
                } else {
                    GridLayoutManager(this?.context,4)
                }
            this?.setHasFixedSize(true)
            this?.adapter = filmAdapter
        }
    }

}
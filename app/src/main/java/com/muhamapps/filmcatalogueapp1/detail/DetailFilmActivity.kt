package com.muhamapps.filmcatalogueapp1.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.muhamapps.filmcatalogueapp1.R
import com.muhamapps.filmcatalogueapp1.ads.AdsManager
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.utils.NetworkInfo.IMAGE_URL
import com.muhamapps.filmcatalogueapp1.databinding.ActivityDetailFilmBinding
import org.koin.android.ext.android.inject

import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class DetailFilmActivity : AppCompatActivity() {

    private val detailFilmViewModel: DetailFilmViewModel by viewModel()
    private val binding: ActivityDetailFilmBinding by lazy {
        ActivityDetailFilmBinding.inflate(layoutInflater)
    }
    private val adsManager: AdsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adsManager.loadBanner(this, binding.adViewContainer)

        val detailFilm = intent.getParcelableExtra<Film>(EXTRA_DATA)
        showDetailFilm(detailFilm)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showDetailFilm(detailFilm: Film?) {
        detailFilm?.let {
            binding.progressBar.visibility = View.GONE
            supportActionBar?.title = detailFilm.title
            binding.rate.text = detailFilm.rating
            binding.tvDetailDescription.text = detailFilm.description
            Glide.with(this@DetailFilmActivity)
                .load(IMAGE_URL + detailFilm.poster)
                .into(binding.ivDetailImage)

            var statusFavorite = detailFilm.isFavorite
            setStatusFavorite(statusFavorite)
            binding.fab.setOnClickListener {
                statusFavorite = !statusFavorite
                detailFilmViewModel.setFavoriteFilm(detailFilm, statusFavorite)
                setStatusFavorite(statusFavorite)
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white))
        } else {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_favorite_white))
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}

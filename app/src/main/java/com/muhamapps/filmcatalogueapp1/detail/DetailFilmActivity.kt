package com.muhamapps.filmcatalogueapp1.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.muhamapps.filmcatalogueapp1.R
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.utils.NetworkInfo.IMAGE_URL
import com.muhamapps.filmcatalogueapp1.databinding.ActivityDetailFilmBinding

import org.koin.android.viewmodel.ext.android.viewModel

class DetailFilmActivity : AppCompatActivity() {

    private val detailFilmViewModel: DetailFilmViewModel by viewModel()
    private lateinit var binding: ActivityDetailFilmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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

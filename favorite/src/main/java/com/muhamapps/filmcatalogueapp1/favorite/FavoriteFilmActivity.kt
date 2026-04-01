package com.muhamapps.filmcatalogueapp1.favorite

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.ui.FilmAdapter
import com.muhamapps.filmcatalogueapp1.core.ui.FilmShareCallback
import com.muhamapps.filmcatalogueapp1.detail.DetailFilmActivity
import com.muhamapps.filmcatalogueapp1.favorite.databinding.ActivityFavoriteFilmBinding
import com.muhamapps.filmcatalogueapp1.ads.AdsManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFilmActivity : AppCompatActivity(), FilmShareCallback {

    private val favoriteFilmViewModel: FavoriteFilmViewModel by viewModel()
    private val adsManager: AdsManager by lazy { AdsManager() }

    private val binding: ActivityFavoriteFilmBinding by lazy {
        ActivityFavoriteFilmBinding.inflate(layoutInflater)
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Your Favorites"

        loadKoinModules(favoriteFilmModule)

        adsManager.loadBanner(this, binding.adViewContainer)

        getFavoriteData()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onShareClick(data: Film) {
        val mimeType = "text/plain"
        @Suppress("DEPRECATION")
        ShareCompat.IntentBuilder
            .from(this)
            .setType(mimeType)
            .setChooserTitle("Bagikan aplikasi ini sekarang.")
            .setText("Segera daftar kelas ${data.title} di dicoding.com")
            .startChooser()
    }

    private fun getFavoriteData() {
        val filmAdapter = FilmAdapter(this)
        filmAdapter.onItemClick = { selectedData ->
                val intent = Intent(this, DetailFilmActivity::class.java)
                intent.putExtra(DetailFilmActivity.EXTRA_DATA, selectedData)
                startActivity(intent)
            }

            favoriteFilmViewModel.favoriteFilm.observe(this, { dataFilm ->
                filmAdapter.setData(dataFilm)
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
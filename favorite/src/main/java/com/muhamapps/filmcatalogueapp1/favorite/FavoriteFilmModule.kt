package com.muhamapps.filmcatalogueapp1.favorite

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteFilmModule = module {
    viewModel { FavoriteFilmViewModel(get()) }
}
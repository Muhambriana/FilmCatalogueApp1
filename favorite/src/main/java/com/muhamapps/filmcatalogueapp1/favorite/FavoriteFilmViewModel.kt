package com.muhamapps.filmcatalogueapp1.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.muhamapps.filmcatalogueapp1.core.domain.usecase.FilmUseCase

class FavoriteFilmViewModel(filmUseCase: FilmUseCase) : ViewModel() {
    val favoriteFilm = filmUseCase.getFavoriteFilm().asLiveData()
}
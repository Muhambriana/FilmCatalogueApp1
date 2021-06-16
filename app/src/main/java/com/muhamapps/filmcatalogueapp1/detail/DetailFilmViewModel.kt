package com.muhamapps.filmcatalogueapp1.detail

import androidx.lifecycle.ViewModel
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.domain.usecase.FilmUseCase

class DetailFilmViewModel(private val filmUseCase: FilmUseCase) : ViewModel() {
    fun setFavoriteFilm(film: Film, newStatus:Boolean) =
        filmUseCase.setFavoriteFilm(film, newStatus)
}

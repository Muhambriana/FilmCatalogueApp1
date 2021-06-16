package com.muhamapps.filmcatalogueapp1.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.muhamapps.filmcatalogueapp1.core.domain.usecase.FilmUseCase

class HomeViewModel(filmUseCase: FilmUseCase) : ViewModel() {
    val film = filmUseCase.getAllFilm().asLiveData()
}
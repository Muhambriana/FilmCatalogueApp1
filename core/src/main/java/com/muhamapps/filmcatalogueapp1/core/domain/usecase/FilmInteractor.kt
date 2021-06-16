package com.muhamapps.filmcatalogueapp1.core.domain.usecase

import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.domain.repository.IFilmRepository

class FilmInteractor(private val filmRepository: IFilmRepository): FilmUseCase {

    override fun getAllFilm() = filmRepository.getAllFilm()

    override fun getFavoriteFilm() = filmRepository.getFavoriteFilm()

    override fun setFavoriteFilm(film: Film, state: Boolean) = filmRepository.setFavoriteFilm(film, state)
}
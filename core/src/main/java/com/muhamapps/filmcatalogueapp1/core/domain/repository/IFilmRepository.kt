package com.muhamapps.filmcatalogueapp1.core.domain.repository

import com.muhamapps.filmcatalogueapp1.core.data.Resource
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import kotlinx.coroutines.flow.Flow

interface IFilmRepository {

    fun getAllFilm(): Flow<Resource<List<Film>>>

    fun getFavoriteFilm(): Flow<List<Film>>

    fun setFavoriteFilm(film: Film, state: Boolean)

}
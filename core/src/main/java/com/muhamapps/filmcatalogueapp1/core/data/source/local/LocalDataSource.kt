package com.muhamapps.filmcatalogueapp1.core.data.source.local

import com.muhamapps.filmcatalogueapp1.core.data.source.local.entity.FilmEntity
import com.muhamapps.filmcatalogueapp1.core.data.source.local.room.FilmDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val filmDao: FilmDao) {

    fun getAllFilm(): Flow<List<FilmEntity>> = filmDao.getAllFilm()

    fun getFavoriteFilm(): Flow<List<FilmEntity>> = filmDao.getFavoriteFilm()

    suspend fun insertFilm(filmList: List<FilmEntity>) = filmDao.insertFilm(filmList)

    fun setFavoriteFilm(film: FilmEntity, newState: Boolean) {
        film.isFavorite = newState
        filmDao.updateFavoriteFilm(film)
    }
}
package com.muhamapps.filmcatalogueapp1.core.utils

import com.muhamapps.filmcatalogueapp1.core.data.source.local.entity.FilmEntity
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.response.FilmResponse
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film

object DataMapper {
    fun mapResponsesToEntities(input: List<FilmResponse>): List<FilmEntity> {
        val filmList = ArrayList<FilmEntity>()
        input.map {
            val film = FilmEntity(
                movieId = it.movieId,
                title = it.title ,
                rating = it.rating,
                description = it.description,
                poster = it.poster,
                isFavorite = false
            )
            filmList.add(film)
        }
        return filmList
    }

    fun mapEntitiesToDomain(input: List<FilmEntity>): List<Film> =
        input.map {
            Film(
                movieId = it.movieId,
                title = it.title,
                rating = it.rating,
                description = it.description,
                poster = it.poster,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(input: Film) = FilmEntity(
        movieId = input.movieId,
        title = input.title ,
        rating = input.rating,
        description = input.description,
        poster = input.poster,
        isFavorite = input.isFavorite
    )
}
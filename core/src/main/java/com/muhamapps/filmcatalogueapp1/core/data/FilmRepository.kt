package com.muhamapps.filmcatalogueapp1.core.data

import com.muhamapps.filmcatalogueapp1.core.data.source.local.LocalDataSource
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.RemoteDataSource
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.network.ApiResponse
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.response.FilmResponse
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.domain.repository.IFilmRepository
import com.muhamapps.filmcatalogueapp1.core.utils.AppExecutors
import com.muhamapps.filmcatalogueapp1.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilmRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IFilmRepository {

    override fun getAllFilm(): Flow<Resource<List<Film>>> =
        object : NetworkBoundResource<List<Film>, List<FilmResponse>>() {
            override fun loadFromDB(): Flow<List<Film>> {
                return localDataSource.getAllFilm().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Film>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<FilmResponse>>> =
                remoteDataSource.getAllFilm()

            override suspend fun saveCallResult(data: List<FilmResponse>) {
                val filmList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertFilm(filmList)
            }
        }.asFlow()

    override fun getFavoriteFilm(): Flow<List<Film>> {
        return localDataSource.getFavoriteFilm().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteFilm(film: Film, state: Boolean) {
        val filmEntity = DataMapper.mapDomainToEntity(film)
        appExecutors.diskIO().execute { localDataSource.setFavoriteFilm(filmEntity, state) }
    }
}


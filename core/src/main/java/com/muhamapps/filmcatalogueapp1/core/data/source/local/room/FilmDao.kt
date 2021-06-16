package com.muhamapps.filmcatalogueapp1.core.data.source.local.room

import androidx.room.*
import com.muhamapps.filmcatalogueapp1.core.data.source.local.entity.FilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Query("SELECT * FROM movie")
    fun getAllFilm(): Flow<List<FilmEntity>>

    @Query("SELECT * FROM movie where isFavorite = 1")
    fun getFavoriteFilm(): Flow<List<FilmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: List<FilmEntity>)

    @Update
    fun updateFavoriteFilm(film: FilmEntity)
}
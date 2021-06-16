package com.muhamapps.filmcatalogueapp1.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class FilmEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieId")
    var movieId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "rating")
    val rating  : String,

    @ColumnInfo(name = "overview")
    val description: String,

    @ColumnInfo(name = "poster")
    val poster: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)
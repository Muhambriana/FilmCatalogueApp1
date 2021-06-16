package com.muhamapps.filmcatalogueapp1.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class FilmResponse(
    @field:SerializedName("id")
    val movieId: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("vote_average")
    val rating  : String,

    @field:SerializedName("overview")
    val description: String,

    @field:SerializedName("poster_path")
    val poster: String,
)
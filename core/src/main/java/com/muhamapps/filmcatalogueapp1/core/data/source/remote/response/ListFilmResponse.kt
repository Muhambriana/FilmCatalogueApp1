package com.muhamapps.filmcatalogueapp1.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListFilmResponse(

    @field:SerializedName("results")
    val results: List<FilmResponse>
)
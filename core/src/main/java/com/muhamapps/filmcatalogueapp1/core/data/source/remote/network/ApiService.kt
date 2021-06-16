package com.muhamapps.filmcatalogueapp1.core.data.source.remote.network

import com.muhamapps.filmcatalogueapp1.core.data.source.remote.response.ListFilmResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/upcoming")
    suspend fun getList(
        @Query("api_key") apiKey: String
    ): ListFilmResponse
}
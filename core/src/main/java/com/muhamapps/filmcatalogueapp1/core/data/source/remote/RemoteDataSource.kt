package com.muhamapps.filmcatalogueapp1.core.data.source.remote

import android.util.Log
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.network.ApiResponse
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.network.ApiService
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.response.FilmResponse
import com.muhamapps.filmcatalogueapp1.core.utils.NetworkInfo.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getAllFilm(): Flow<ApiResponse<List<FilmResponse>>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getList(API_KEY)
                val dataArray = response.results
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
package com.example.giphyapp.data.api

import com.example.giphyapp.data.api.model.Giph
import com.example.giphyapp.data.api.model.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {

    @GET("gifs/search")
    fun requestGiffs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") perPage: Int,
        @Query("offset") offset: Int
    ): Single<ResponseWrapper<List<Giph>>>

    @GET("gifs/trending")
    fun requestTrending(
        @Query("api_key") apiKey: String,
        @Query("limit") perPage: Int,
        @Query("offset") offset: Int
    ): Single<ResponseWrapper<List<Giph>>>

}
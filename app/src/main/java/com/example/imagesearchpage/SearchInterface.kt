package com.example.imagesearchpage

import com.example.imagesearchpage.data.image.ImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchInterface {

    @GET("search/image")
    fun getNowPlaying(
        @Header("Authorization") authorization: String,
        @Query("query") search: String
    ): Call<ImageResponse>
//
//    @GET("/vclip")
//    fun detailMovie(
//        @Path("movie_id") movie_id: Long,
//        @Query("api_key") api_key: String,
//        @Query("language") language: String,
//        @Query("append_to_response") append_to_response: String,
//    ): Call<DetailMovieResponse>
}

package com.example.imagesearchpage

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    var mRetrofit: Retrofit? = null
    private const val SEARCH_URL = "https://dapi.kakao.com/v2/"

    fun initRetrofit(): Retrofit {
        if (mRetrofit == null) {
            mRetrofit = Retrofit.Builder()
                .baseUrl(SEARCH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return mRetrofit!!
    }

}
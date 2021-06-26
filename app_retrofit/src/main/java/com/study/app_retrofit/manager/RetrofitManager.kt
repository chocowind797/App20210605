package com.study.app_retrofit.manager

import com.study.app_retrofit.api.JsonPlaceHolderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 會以 Singleton 來設計
class RetrofitManager {
    private val placeHolderApi: JsonPlaceHolderApi
    val api: JsonPlaceHolderApi
        get() = placeHolderApi

    // Singleton
    companion object {
        val instance = RetrofitManager()
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        placeHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
    }
}
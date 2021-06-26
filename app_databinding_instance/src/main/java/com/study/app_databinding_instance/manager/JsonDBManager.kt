package com.study.app_databinding_instance.manager

import com.study.app_databinding_instance.service.JsonDBApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JsonDBManager {
    private val jsonApi: JsonDBApi
    val api: JsonDBApi
    get() = jsonApi

    companion object {
        val instance = JsonDBManager()
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        jsonApi = retrofit.create(JsonDBApi::class.java)
    }
}
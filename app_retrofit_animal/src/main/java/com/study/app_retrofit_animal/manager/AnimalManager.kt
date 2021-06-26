package com.study.app_retrofit_animal.manager

import com.study.app_retrofit_animal.api.AnimalApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimalManager {
    private val animalApi: AnimalApi
    val api: AnimalApi
        get() = animalApi

    companion object {
        val instance = AnimalManager()
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.coa.gov.tw/Service/OpenData/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        animalApi = retrofit.create(AnimalApi::class.java)
    }
}
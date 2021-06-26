package com.example.app_transservice.service

import com.example.app_transservice.model.Animal
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class AnimalService(val path: String) {
    fun getAnimals(): List<Animal> {
        val client = OkHttpClient()

        val request = Request.Builder().url(path).build()

        client.newCall(request).execute().use {
            val json = it.body!!.string()
            return Gson().fromJson(json, Array<Animal>::class.java).toList()
        }
    }
}
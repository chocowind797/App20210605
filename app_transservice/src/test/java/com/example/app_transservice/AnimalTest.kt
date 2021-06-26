package com.example.app_transservice

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

fun main() {
    val path = "https://data.coa.gov.tw/Service/OpenData/TransService.aspx?UnitId=QcbUEzN6E6DL"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(path)
        .build()

    client.newCall(request).execute().use {
        val json = it.body!!.toString()
        val animal: List<Animal> = Gson().fromJson(json, Array<Animal>::class.java).toList()

        println(animal)
    }
}
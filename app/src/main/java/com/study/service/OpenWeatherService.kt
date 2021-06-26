package com.study.service

import com.google.gson.JsonParser
import com.study.model.OpenWeather
import okhttp3.OkHttpClient
import okhttp3.Request

class OpenWeatherService(val appid: String, val path: String) {
    fun getOpenWeather(q: String): OpenWeather {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(path.format(q, appid))
            .build()
        client.newCall(request).execute().use {
            val json = it.body!!.string()

            val root = JsonParser.parseString(json).asJsonObject
            val name = root["name"].toString().replace("\"", "")
            val dt = root["dt"].asInt
            val country = root.getAsJsonObject("sys")["country"].toString().replace("\"", "")
            val clouds_all = root.getAsJsonObject("clouds")["all"].asInt

            val weather = root.getAsJsonArray("weather")[0].asJsonObject
            val weather_main = weather["main"].toString().replace("\"", "")
            val weather_description = weather["description"].toString().replace("\"", "")
            val weather_icon = weather["icon"].toString().replace("\"", "")

            val main = root.getAsJsonObject("main")
            val main_temp = main["temp"].asDouble
            val main_feels_like = main["feels_like"].asDouble
            val main_humidity = main["humidity"].asDouble

            val ow = OpenWeather(
                name,
                country,
                dt,
                clouds_all,
                weather_main,
                weather_description,
                weather_icon,
                main_temp,
                main_feels_like,
                main_humidity
            )
            return ow
        }

    }
}
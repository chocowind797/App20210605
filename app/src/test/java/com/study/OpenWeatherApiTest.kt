package com.study

import com.google.gson.JsonParser
import com.study.api.OpenWeather
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*


fun main() {
    val q = "taoyuan,tw"
    val appid = "3341c6f6692890d3fcf457b8e3b3aa06"
    val path =
        "http://api.openweathermap.org/data/2.5/weather?q=$q&appid=$appid"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(path)
        .build()
    client.newCall(request).execute().use {
        val json = it.body!!.string()
        println("Json: $json")

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

        println(name)
        println(country)
        println(dt)
        println(SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date(dt*1000L)))
        println(clouds_all)
        println(weather_main)
        println(weather_description)
        println(weather_icon)
        println(main_temp)
        println(main_feels_like)
        println(main_humidity)

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

        println(ow)
        println(ow.weather_icon_url)
    }
}
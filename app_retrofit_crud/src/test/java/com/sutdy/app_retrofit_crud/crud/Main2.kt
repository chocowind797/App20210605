package com.sutdy.app_retrofit_crud.crud

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.math.BigDecimal

interface OpenWeatherService {
    @GET
    fun getStringResponse(@Url url: String?): Call<String>
}

fun main() {
    // https://api.openweathermap.org/data/2.5/weather?q=taoyuan,tw&appid=3341c6f6692890d3fcf457b8e3b3aa06
    // 透過 Retrofit 取得 temp 現在溫度
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val api = retrofit.create(OpenWeatherService::class.java)

    val string = api.getStringResponse("weather?q=taoyuan,tw&appid=3341c6f6692890d3fcf457b8e3b3aa06").execute().body().toString()

    val root = JsonParser.parseString(string).asJsonObject
    val temp = root.getAsJsonObject("main")["temp"].asDouble
    println(temp - 273.15)
}
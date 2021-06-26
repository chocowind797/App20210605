package com.study.model

data class OpenWeather(
    val name: String,
    val country: String,
    val dt: Int,
    val clouds_all: Int,
    val weather_main: String,
    val weather_description: String,
    val weather_icon: String,
    val main_temp: Double,
    val main_feels_like: Double,
    val main_humidity: Double
){
    val weather_icon_url: String = "http://openweathermap.org/img/wn/$weather_icon@4x.png"
}

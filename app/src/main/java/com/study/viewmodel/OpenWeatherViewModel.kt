package com.study.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.study.model.OpenWeather

class OpenWeatherViewModel: ViewModel() {
    var ow: OpenWeather? = null

    val current_imageUrl: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val current_name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val current_log: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
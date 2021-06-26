package com.example.app_transservice.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_transservice.model.Animal

class AnimalViewModel: ViewModel() {
    var animals: List<Animal>? = null
    var animal: Animal? = null

    val currentImageUrl: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currentInfo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
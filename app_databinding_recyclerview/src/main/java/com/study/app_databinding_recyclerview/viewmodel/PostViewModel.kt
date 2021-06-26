package com.study.app_databinding_recyclerview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.study.app_databinding_recyclerview.model.Post

class PostViewModel: ViewModel() {
    var posts = MutableLiveData<MutableList<Post>>()

    init {
        posts.value = ArrayList()
    }

    fun defaultData() {
        if(posts.value!!.size == 0) {
            val faker = Faker().book()
            val tempList = mutableListOf(
                Post(1, faker.title(), faker.author()),
                Post(2, faker.title(), faker.author()),
                Post(3, faker.title(), faker.author())
            )
            posts.postValue(tempList)
        }
    }
}
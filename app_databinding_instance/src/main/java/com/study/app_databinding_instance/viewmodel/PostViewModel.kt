package com.study.app_databinding_instance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.study.app_databinding_instance.model.Post

class PostViewModel: ViewModel() {
    var posts = MutableLiveData<MutableList<Post>>()

    init {
        posts.value = ArrayList()
    }
}
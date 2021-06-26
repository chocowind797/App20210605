package com.study.app_databinding_retrofit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.study.app_databinding_retrofit.model.Post

class PostViewModel: ViewModel() {
    var post = MutableLiveData<Post>()
}
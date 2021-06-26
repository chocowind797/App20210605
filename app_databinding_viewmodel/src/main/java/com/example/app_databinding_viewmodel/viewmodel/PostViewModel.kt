package com.example.app_databinding_viewmodel.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_databinding_viewmodel.model.Post
import com.github.javafaker.Faker
import kotlin.random.Random

class PostViewModel: ViewModel() {
    var post = MutableLiveData<Post>()

    fun click() {
        val jf = Faker()
        post.value = Post(
            Random.nextInt(100),
            jf.book().title(),
            jf.book().author()
        )
    }

    fun click2(view: View) {
        val jf = Faker()
        post.value = Post(
            Random.nextInt(100),
            jf.book().title(),
            jf.book().author()
        )
    }
}
package com.study.app_databinding_retrofit.service

import com.study.app_databinding_retrofit.model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonDBApi {
    @GET("/posts/{id}")
    fun getPost(@Path("id") id: Int): Call<Post>

    @GET("/posts")
    fun getPosts(): Call<List<Post>>
}
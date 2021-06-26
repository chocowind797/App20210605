package com.study.app_databinding_instance.service

import com.study.app_databinding_instance.model.Post
import retrofit2.Call
import retrofit2.http.*

interface JsonDBApi {
    @GET("/posts/{id}")
    fun getPost(@Path("id") id: Int): Call<Post>

    @GET("/posts")
    fun getPosts(): Call<List<Post>>

    @POST("/posts")
    fun addPost(@Body post: Post): Call<Post>

    @DELETE("/posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Void>
}
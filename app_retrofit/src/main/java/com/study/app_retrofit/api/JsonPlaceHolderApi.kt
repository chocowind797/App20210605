package com.study.app_retrofit.api

import com.study.app_retrofit.model.Comment
import com.study.app_retrofit.model.Photo
import com.study.app_retrofit.model.Post
import com.study.app_retrofit.model.users.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

// API 的目的是為了給 client 調用
interface JsonPlaceHolderApi {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("comments")
    fun getComments(): Call<List<Comment>>

    @GET("Posts")
    fun getPosts(
        @Query("userId") userId: Int,
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Call<List<Post>>

    @GET("posts")
    fun getPosts(
        @Query("userId") userId: Array<Int>,
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Call<List<Post>>

    @GET("/posts/{id}/comments")
    fun getComments(@Path("id") id: Int): Call<List<Comment>>

    // 查詢指定 URL 字串
    @GET
    fun getComments(@Url url: String): Call<List<Comment>>

    // 查詢 Comments
    @GET("comments")
    fun getComments(@QueryMap params: Map<String, String>): Call<List<Comment>>

    // 單筆查詢 ex: posts/2
    @GET("posts/{id}")
    fun getPosts(@Path("id") id: Int): Call<Post>

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{id}")
    fun getUsers(@Path("id") id: Int): Call<User>

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>
}
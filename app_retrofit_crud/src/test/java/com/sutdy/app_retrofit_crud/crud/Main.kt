package com.sutdy.app_retrofit_crud.crud

import com.sutdy.app_retrofit_crud.crud.users.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// API
interface JsonPlaceHolderService {
    @GET("/posts/{id}")
    fun getPost(@Path("id") id: Int): Call<Post>

    @POST("/posts")
    fun createPost(@Body post: Post): Call<Post>

    @FormUrlEncoded
    @POST("/posts")
    fun createPost(
        @Field("userId") userId: Int,
//        @Field("id") id: Int,
        @Field("title") title: String,
        @Field("body") body: String
    ): Call<Post>

    @Headers("Static-Header: 123") // 預先載入好 header
    @PUT("/posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body body: Post): Call<Post>

    @PUT("/posts/{id}")  // 動態 header
    fun updatePost(@Header("Dynamic-Header") header: String, @Path("id") id: Int, @Body body: Post): Call<Post>

    @PATCH("/posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body body: Post): Call<Post>

    @DELETE("/posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Void>

    @PATCH("/users/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @PATCH("/users/{id}")
    fun updateCityByUser(@Path("id") id: Int, @Body user: User): Call<User>
}

fun main() {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .addInterceptor {
            // User-Agent 隨意加上字, 給網頁偵測是否為瀏覽器
            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()
                .header("User-Agent", "chrome")
                .build()

            it.proceed(newRequest)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val api = retrofit.create(JsonPlaceHolderService::class.java)

    println("CRUD...")

//    // Read
//    val post = api.getPost(1)
//    println(post.execute().body())

//    // create 1
//    val post = Post(23, 0, "New Title", "new Body")
//    println(api.createPost(post).execute().isSuccessful)

//    // create 2 -> 使用 url-encode create
//    println(api.createPost(24, "new Title2", "new Body2").execute().isSuccessful)

//    // put 全部修改
//    val post = api.getPost(1).execute().body()
//    println(post)
//    if(post != null) {
//        post.userId = 99
//        post.title = "AAA"
//        post.body = "BBB"
////        println(api.updatePost(1, post).execute().isSuccessful)
//        println(api.updatePost("I'm chrome 5678.", 1, post).execute().isSuccessful)
//    }

//    // patch 部分修改
//    val post = Post(15, 1, "CCC", null)
//    println(api.patchPost(1, post).execute().isSuccessful)

//    // delete
//    println(api.deletePost(1).execute().isSuccessful)

    // updateCity
    val user = api.getUser(1).execute().body()
    if(user != null) {
        user.address.city = "Taoyuan"
        println(api.updateCityByUser(1, user).execute().isSuccessful)
    }

}
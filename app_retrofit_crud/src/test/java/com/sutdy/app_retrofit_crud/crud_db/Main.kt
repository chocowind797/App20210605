package com.sutdy.app_retrofit_crud.crud_db

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface JsonDBService {
    @GET("/users")
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @POST("/users")
    fun createUser(
        @Body user: User
    ): Call<User>

    @FormUrlEncoded
    @PATCH("/users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Field("name") name: String?,
        @Field("age") age: Int?
    ): Call<User>

    @PUT("/users/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("/users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>
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
        .baseUrl("http://localhost:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val api = retrofit.create(JsonDBService::class.java)

    // 查詢
    println(api.getUsers().execute().isSuccessful)

//    // 新增一筆 id = 2, name = "Marry", age = 19
//    println(api.createUser(User(2, "Marry", 19)).execute().isSuccessful)
//
//    // 修改 put id = 2, name = "Jane", age = 20
//    val user = api.getUser(2).execute().body()
//    if (user != null) {
//        user.age = 20
//        user.name = "Jane"
//        println(api.updateUser(2, user).execute().isSuccessful)
//    }
//
//    // 修改 patch id = 2, age = 21
//    println(api.updateUser(2, null, 21).execute().isSuccessful)
//
//    // 刪除 delete
//    println(api.deleteUser(2).execute().isSuccessful)
}
package com.study.app_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.study.app_retrofit.manager.RetrofitManager
import com.study.app_retrofit.model.Comment
import com.study.app_retrofit.model.Photo
import com.study.app_retrofit.model.Post
import com.study.app_retrofit.model.users.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter()
            adapter = recyclerViewAdapter

            val divider = DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        GlobalScope.launch {
            val api = RetrofitManager.instance.api

            btn_posts.setOnClickListener {
//                api.getPosts().enqueue(getPosts())
//                api.getPosts(2, "id", "desc").enqueue(getPosts())
//                api.getPosts(arrayOf(2, 4), "id", "desc").enqueue(getPosts())

                api.getPosts(2).enqueue(getPost())
            }

            btn_comments.setOnClickListener {
//                api.getComments().enqueue(getComments())
//                api.getComments("/posts/2/comments").enqueue(getComments())

//                api.getComments(3).enqueue(getComments())

                val params = HashMap<String, String>()
                params["postId"] = "4"
                params["_sort"] = "id"
                params["_order"] = "desc"

                api.getComments(params).enqueue(getComments())
            }

            btn_users.setOnClickListener {
                api.getUsers().enqueue(getUsers())
            }

            btn_photos.setOnClickListener {
                api.getPhotos().enqueue(getPhotos())
            }
        }
    }

    fun getPhotos(): Callback<List<Photo>> {
        return object : Callback<List<Photo>> {
            // Server 端有回應
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity", "Is not successful: ${response.code()}")
                    return
                }
                val photos = response.body()
                Log.d("MainActivity-callback", photos!!.size.toString())
                Log.d("MainActivity-callback", photos.toString())

                // UI 呈現
                runOnUiThread {
                    tv_posts.visibility = View.GONE
                    recycler_view.visibility = View.VISIBLE
                    recyclerViewAdapter.setListData(photos)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }

            // 無法連線(Ex: 404 Error)
            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.d("MainActivity-callback", "Fail: ${t.message}")
            }
        }
    }

    fun getUsers(): Callback<List<User>> {
        return object : Callback<List<User>> {
            // Server 端有回應
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity", "Is not successful: ${response.code()}")
                    return
                }
                val users = response.body()
                Log.d("MainActivity-callback", users!!.size.toString())
                Log.d("MainActivity-callback", users.toString())

                // UI 呈現
                runOnUiThread {
                    tv_posts.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                    tv_posts.text = users.toString()
                }
            }

            // 無法連線(Ex: 404 Error)
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("MainActivity-callback", "Fail: ${t.message}")
            }
        }
    }

    fun getComments(): Callback<List<Comment>> {
        return object : Callback<List<Comment>> {
            // Server 端有回應
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity", "Is not successful: ${response.code()}")
                    return
                }
                val comments = response.body()
                Log.d("MainActivity-callback", comments!!.size.toString())
                Log.d("MainActivity-callback", comments.toString())

                // UI 呈現
                runOnUiThread {
                    tv_posts.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                    tv_posts.text = comments.toString()
                }
            }

            // 無法連線(Ex: 404 Error)
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("MainActivity-callback", "Fail: ${t.message}")
            }
        }
    }

    fun getPost(): Callback<Post> {
        return object : Callback<Post> {
            // Server 端有回應
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity", "Is not successful: ${response.code()}")
                    return
                }
                val post = response.body()
                Log.d("MainActivity-callback", post.toString())

                // UI 呈現
                runOnUiThread {
                    tv_posts.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                    tv_posts.text = post.toString()
                }
            }

            // 無法連線(Ex: 404 Error)
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("MainActivity-callback", "Fail: ${t.message}")
            }
        }
    }

    fun getPosts(): Callback<List<Post>> {
        return object : Callback<List<Post>> {
            // Server 端有回應
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity", "Is not successful: ${response.code()}")
                    return
                }
                val posts = response.body()
                Log.d("MainActivity-callback", posts!!.size.toString())
                Log.d("MainActivity-callback", posts.toString())

                // UI 呈現
                runOnUiThread {
                    tv_posts.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                    tv_posts.text = posts.toString()
                }
            }

            // 無法連線(Ex: 404 Error)
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("MainActivity-callback", "Fail: ${t.message}")
            }
        }
    }
}
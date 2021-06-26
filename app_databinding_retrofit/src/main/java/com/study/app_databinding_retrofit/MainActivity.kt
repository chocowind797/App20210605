package com.study.app_databinding_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.study.app_databinding_retrofit.databinding.ActivityMainBinding
import com.study.app_databinding_retrofit.manager.JsonDBManager
import com.study.app_databinding_retrofit.viewmodel.PostViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            this.lifecycleOwner = this@MainActivity
            this.vm = viewModel
        }
    }

    fun onClick(view: View) {
        GlobalScope.launch {
            val api = JsonDBManager.instance.api

            val size = api.getPosts().execute().body()?.size
            if(size != null) {
                val post = api.getPost(Random.nextInt(size)+1).execute().body()
                runOnUiThread {
                    viewModel.post.value = post
                }
            }
        }
    }
}
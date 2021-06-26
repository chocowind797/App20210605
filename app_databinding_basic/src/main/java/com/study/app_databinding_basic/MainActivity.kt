package com.study.app_databinding_basic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.study.app_databinding_basic.model.Post
import com.study.app_databinding_basic.databinding.ActivityMainBinding
import kotlin.random.Random

/*
* 要使用 Databinding:
* 1. 在 gradle kotlinOptions{} 下面新增 dataBinding {
        enabled = true
    }
* 2. xml 要更換為 Databinding 格式
* */

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 建立資料
        val post = Post(12, "kotlin", "Java")

        // 繫結資料 Binding data
        mBinding.post = post
    }

    fun click(view: View) {
        val post = Post(Random.nextInt(100), "Python", "ABC")
        Toast.makeText(applicationContext, post.toString(), Toast.LENGTH_SHORT).show()
        mBinding.post = post
    }
}
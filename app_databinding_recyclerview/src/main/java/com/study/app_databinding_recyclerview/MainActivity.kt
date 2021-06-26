package com.study.app_databinding_recyclerview

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.javafaker.Faker
import com.study.app_databinding_recyclerview.databinding.RecyclerviewRowBinding
import com.study.app_databinding_recyclerview.model.Post
import com.study.app_databinding_recyclerview.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.notify

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(divider)

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        viewModel.posts.observe(this) {
            recyclerView.adapter = PostAdapter(it)
        }

        viewModel.defaultData()
    }

    fun onClick(view: View) {
        val faker = Faker()
        viewModel.posts.value?.add(Post(
            viewModel.posts.value!!.size + 1,
            faker.book().title(),
            faker.book().author()
        ))
        recyclerView.adapter?.notifyDataSetChanged()
    }

    class PostAdapter(private var items: List<Post>) :
        RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
        inner class MyViewHolder(var dataBinding: ViewDataBinding) :
            RecyclerView.ViewHolder(dataBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.recyclerview_row, parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val binding: ViewDataBinding = holder.dataBinding
            binding.setVariable(BR.post, items[position])   // BR -> 自動產生的 resource ID 類別
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }
}
package com.study.app_databinding_instance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.javafaker.Faker
import com.mifmif.common.regex.Main
import com.study.app_databinding_instance.manager.JsonDBManager
import com.study.app_databinding_instance.model.Post
import com.study.app_databinding_instance.service.JsonDBApi
import com.study.app_databinding_instance.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            recyclerView.adapter = PostAdapter(it, this)
        }
    }

    fun onAddClick(view: View) {
        GlobalScope.launch {
            val faker = Faker()
            val post = Post(
                viewModel.posts.value!!.size + 1,
                faker.book().title(),
                faker.book().author()
            )
            val api = JsonDBManager.instance.api
            val response = api.addPost(post).execute()
            runOnUiThread {
                viewModel.posts.value?.add(response.body() ?: post)
                recyclerView.adapter?.notifyDataSetChanged()
                Toast.makeText(applicationContext, response.isSuccessful.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onListClick(view: View) {
        listItem()
    }

    fun listItem() {
        GlobalScope.launch {
            val api = JsonDBManager.instance.api
            val response = api.getPosts().execute()
            runOnUiThread {
                viewModel.posts.value = response.body()?.toMutableList()
                Toast.makeText(applicationContext, response.isSuccessful.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun itemDelete(id: Int) {
        GlobalScope.launch {
            val api = JsonDBManager.instance.api
            val status = api.deletePost(id).execute().isSuccessful
            listItem()
            runOnUiThread {
                Toast.makeText(applicationContext, status.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    class PostAdapter(private var items: List<Post>, private val main: MainActivity) :
        RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
        inner class MyViewHolder(var dataBinding: ViewDataBinding) :
            RecyclerView.ViewHolder(dataBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val dataBinding: ViewDataBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.recyclerview_row, parent, false
                )

            dataBinding.setVariable(BR.main, main)
            return MyViewHolder(dataBinding)
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
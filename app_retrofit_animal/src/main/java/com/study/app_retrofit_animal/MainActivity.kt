package com.study.app_retrofit_animal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.app_retrofit_animal.manager.AnimalManager
import com.study.app_retrofit_animal.model.Animal
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

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter()
            adapter = recyclerViewAdapter
        }

        GlobalScope.launch {
            val api = AnimalManager.instance.api
            val animals = api.getAnimals(resources.getString(R.string.uid)).execute().body()
            runOnUiThread {
                title = "寵物領養筆數: ${animals!!.size}"
                recyclerViewAdapter.setListData(animals)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}
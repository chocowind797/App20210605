package com.example.app_transservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.app_transservice.model.Animal
import com.example.app_transservice.service.AnimalService
import com.example.app_transservice.viewmodel.AnimalViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: AnimalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(AnimalViewModel::class.java)

        viewModel.currentImageUrl.observe(this, Observer {
            if (it.isEmpty()) {
                iv_album.setImageResource(R.drawable.image_not_found)
            } else {
                Picasso.get().load(it).into(iv_album)
            }
        })
        viewModel.currentInfo.observe(this, Observer {
            tv_info.text = it
        })

        GlobalScope.launch {
            val path = resources.getString(R.string.path)
            viewModel.animals = AnimalService(path).getAnimals()
            val r = Random.nextInt(viewModel.animals!!.size)
            viewModel.animal = viewModel.animals!![r]
            runOnUiThread {
                viewModel.currentImageUrl.value = viewModel.animal!!.album_file
                viewModel.currentInfo.value = viewModel.animal.toString()
            }
        }

        iv_album.setOnClickListener {
            val r = Random.nextInt(viewModel.animals!!.size)
            viewModel.animal = viewModel.animals?.get(r)
            runOnUiThread {
                viewModel.currentImageUrl.value = viewModel.animal!!.album_file
                viewModel.currentInfo.value = viewModel.animal.toString()
                tv_info.visibility = View.GONE
            }
        }

        iv_album.setOnLongClickListener {
            if(tv_info.isVisible) {
                tv_info.visibility = View.GONE
            }else{
                tv_info.visibility = View.VISIBLE
            }
            true
        }
    }
}
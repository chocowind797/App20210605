package com.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import com.study.model.OpenWeather
import com.study.service.OpenWeatherService
import com.study.viewmodel.OpenWeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: OpenWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(OpenWeatherViewModel::class.java)

        viewModel.current_imageUrl.observe(this, Observer {
            Picasso.get().load(it).into(iv_icon)
        })

        viewModel.current_name.observe(this, Observer {
            tv_name.text = it.toString()
        })

        viewModel.current_log.observe(this, Observer {
            tv_log.text = it.toString()
        })
    }

    fun changeOpenWeather(view: View) {
        val q = view.tag.toString()
        Toast.makeText(applicationContext, q, Toast.LENGTH_SHORT).show()

        GlobalScope.launch {
            val appid = resources.getString(R.string.appid)
            val path = resources.getString(R.string.path)
            viewModel.ow = OpenWeatherService(appid, path).getOpenWeather(q)
            runOnUiThread {
                viewModel.current_imageUrl.value = viewModel.ow!!.weather_icon_url
                viewModel.current_name.value = viewModel.ow!!.name
                viewModel.current_log.value = viewModel.ow.toString()
            }
        }
    }
}

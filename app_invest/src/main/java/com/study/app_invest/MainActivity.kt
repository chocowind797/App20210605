package com.study.app_invest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://10.0.2.2:8080/AppWebBackend/investor_transactionlog.jsp"

        web_view.settings.javaScriptEnabled = true
        web_view.settings.domStorageEnabled = true
//        web_view.webChromeClient =
        web_view.loadUrl(url)
    }
}
package com.study.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_username.setText("John")

        context = this
    }

    fun orderLogin(view: View) {
        val username = et_username.text.toString()
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("username", username)

        startActivity(intent)
    }

    fun consoleLogin(view: View) {
        val intent = Intent(context, MainActivity::class.java)

        startActivity(intent)
    }
}
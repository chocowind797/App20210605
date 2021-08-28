package com.study.app_login_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val message = intent.getStringExtra("message")
        val currentUser = intent.getParcelableExtra<FirebaseUser>("currentUser")
        if(message != null)
            tv_result.text = message

        if(currentUser != null) {
            btn_resend.visibility = View.VISIBLE
            btn_resend.setOnClickListener {
                currentUser.sendEmailVerification()
                Toast.makeText(this, "已重新發送驗證信件", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun back(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
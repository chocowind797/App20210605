package com.study.app_login_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        context = this
        auth = Firebase.auth
    }

    fun forgot(view: View) {
        auth.sendPasswordResetEmail(et_forgot_email.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(context, "已發送更改密碼信件", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ResultActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(context, "發送更改密碼信件失敗", Toast.LENGTH_SHORT).show()
                }
            }

        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
        finish()
    }
}
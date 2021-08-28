package com.study.app_login_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        context = this
        auth = Firebase.auth
    }

    fun register(view: View) {
        val email = et_register_email.text.toString()
        val password = et_register_password.text.toString()
        var message: String? = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    message = "註冊成功"
                    // 發送 email 驗證信件
                    it.result?.user?.sendEmailVerification()
                } else {
                    message = "註冊失敗"
                }
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("message", message)
                startActivity(intent)
                finish()
            }
    }
}
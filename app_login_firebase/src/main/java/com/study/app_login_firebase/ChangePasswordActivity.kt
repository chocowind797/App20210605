package com.study.app_login_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = Firebase.auth
    }

    fun changePassword(view: View) {
        auth.confirmPasswordReset(ActionCodeSettings.newBuilder().setHandleCodeInApp(true), "ziyun52570")
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Toast.makeText(this, "tr", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, it.result.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}
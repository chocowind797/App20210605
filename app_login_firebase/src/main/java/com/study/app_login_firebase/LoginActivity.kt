package com.study.app_login_firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var setting: SharedPreferences

    companion object {
        private const val DATA = "DATA"
        private const val EMAIL = "EMAIL"
        private const val PASSWORD = "PASSWORD"
        private const val MEMORY = "MEMORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this
        auth = Firebase.auth
        setting = getSharedPreferences(DATA, MODE_PRIVATE)

        et_email.setText(setting.getString(EMAIL, ""))
        et_password.setText(setting.getString(PASSWORD, ""))
        cb_memory.isChecked = setting.getBoolean(MEMORY, false)
    }

    fun signIn(view: View) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    if(it.result?.user?.isEmailVerified == true) {
                        if (cb_memory.isChecked) {
                            setting.edit()
                                .putString(EMAIL, et_email.text.toString())
                                .putString(PASSWORD, et_password.text.toString())
                                .putBoolean(MEMORY, true)
                                .apply()
                        } else {
                            setting.edit()
                                .putString(EMAIL, "")
                                .putString(PASSWORD, "")
                                .putBoolean(MEMORY, false)
                                .apply()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("message", "Email 尚未驗證")
                        intent.putExtra("currentUser", auth.currentUser)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        context, "登入失敗 !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun forgot(view: View) {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(1, 1, 1, "註冊")?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
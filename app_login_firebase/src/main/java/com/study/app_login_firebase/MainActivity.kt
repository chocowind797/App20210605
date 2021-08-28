package com.study.app_login_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth
        val currentUser = auth.currentUser
        Log.d("MainActivity", currentUser.toString())

        if(currentUser == null || !currentUser.isEmailVerified) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

//        updateUI(currentUser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(1, 1, 1, "登出")?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> {
                Firebase.auth.signOut()
                Toast.makeText(context, "已成功登出", Toast.LENGTH_SHORT).show()
                onStart()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
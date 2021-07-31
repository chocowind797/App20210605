package com.study.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var context: Context
    val database = Firebase.database
    val myUserRef = database.getReference("ticketsUser")
    val users = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_username.setText("John")

        context = this

        getPriority()
    }

    private fun getPriority() {
        myUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val username = it.child("name").value.toString()
                    val priority = it.child("priority").value.toString().toInt()

                    users[username] = priority
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkPriority(username: String, priority: Int): Boolean {
        val user_priority = users.getOrDefault(username, 0)
        return user_priority >= priority
    }

    fun orderLogin(view: View) {
        val username = et_username.text.toString()

        if (checkPriority(username, 1)) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("username", username)

            startActivity(intent)
        } else {
            Toast.makeText(context, "無權限訪問", Toast.LENGTH_SHORT).show()
        }
    }

    fun consoleLogin(view: View) {
        val username = et_username.text.toString()

        if (checkPriority(username, 2)) {
            val intent = Intent(context, ConsoleActivity::class.java)
            intent.putExtra("username", username)

            startActivity(intent)
        } else {
            Toast.makeText(context, "無權限訪問", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.study.app_tictactoe_firebase

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val database = Firebase.database
    val myTTTRef = database.getReference("ttt/game")
    val myTTTLastMarkRef = database.getReference("ttt/last_mark")
    val myTTTWinnerRef = database.getReference("ttt/winner")
    val TAG = "MainActivity"
    var winner: String = ""
    lateinit var mark: String
    lateinit var context: Context
    lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        notificationManager = NotificationManagerCompat.from(this)

        btn_mark.setOnClickListener {
            val mark = btn_mark.text.toString()
            if (mark == "O") {
                btn_mark.text = "X"
                btn_mark.tag = "X"
            } else {
                btn_mark.text = "O"
                btn_mark.tag = "O"
            }
        }

        btn_reset.setOnClickListener {
            for (i in 1..9)
                myTTTRef.child("b$i").setValue("")
            myTTTLastMarkRef.setValue("")
            myTTTWinnerRef.setValue("")
        }

        myTTTRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                Log.d(TAG, snapshot.value.toString())
                value.forEach {
                    val id = resources.getIdentifier(it.key, "id", context.packageName)
                    findViewById<Button>(id).text = it.value.toString()
                }
                checkWinner()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        myTTTLastMarkRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mark = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        myTTTWinnerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value.toString() != "") {
                    winner = snapshot.value.toString()
                    sendNotification(winner)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun onClick(view: View) {
        if (mark != btn_mark.tag.toString() && winner != "") {
            myTTTRef.child(view.tag.toString()).setValue(btn_mark.tag.toString())
            myTTTLastMarkRef.setValue(btn_mark.tag.toString())
        }
    }

    fun sendNotification(winner: String) {
        val notification = NotificationCompat.Builder(context, App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Tic Tac Toe Winner")
            .setContentText("$winner is winner.")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager.notify(1001, notification)
    }

    fun checkWinner() {
        var win = false
        val mark = btn_mark.tag.toString()
        if ((b1.text.toString() == mark && b2.text.toString() == mark && b3.text.toString() == mark)
            || (b2.text.toString() == mark && b5.text.toString() == mark && b8.text.toString() == mark)
            || (b3.text.toString() == mark && b6.text.toString() == mark && b9.text.toString() == mark)
            || (b1.text.toString() == mark && b5.text.toString() == mark && b9.text.toString() == mark)
            || (b3.text.toString() == mark && b5.text.toString() == mark && b7.text.toString() == mark)
        )
            win = true

        if(win)
            myTTTWinnerRef.setValue(mark)
    }
}
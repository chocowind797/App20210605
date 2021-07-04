package com.study.app_notification_firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)

        val database = Firebase.database
        val myMessageRef = database.getReference("message")

        btn_submit.setOnClickListener {
            val data = et_message.text.toString()
            myMessageRef.setValue(data)
        }

        // Read from the database
        myMessageRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                tv_message.text = value
                sendByChannel1(value)
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    fun sendByChannel1(message: String) {
        val nowDate: String = SimpleDateFormat("HH:mm", Locale.TAIWAN).format(Date())
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val title = "From Firebase"
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(message)
            .setContentIntent(pIntent)
            .setSubText(nowDate)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        // id 為後續需要刪除通知需要給的資訊
        notificationManager.notify(1001, notification)
    }
}
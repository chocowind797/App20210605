package com.study.app_notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.logging.SimpleFormatter

class MainActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 建構 notificationManager
        notificationManager = NotificationManagerCompat.from(this)
    }

    fun sendByChannel1(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val title = et_title.text.toString()
        val message = et_message.text.toString()
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setOngoing(true)   // 無法刪除
            .setContentText(message)
            .setContentIntent(pIntent)
            .setSubText("2021-7-3")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        // id 為後續需要刪除通知需要給的資訊
        notificationManager.notify(1001, notification)
    }

    fun sendByChannel2(view: View) {
        val title = et_title.text.toString()
        val message = et_message.text.toString()
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_2_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationManager.IMPORTANCE_LOW)
            .build()

        // id 為後續需要刪除通知需要給的資訊
        notificationManager.notify(1002, notification)
    }

    fun deleteFromChannel1(view: View) {
        notificationManager.cancel(1001)
    }

    fun continueFromChannel2(view: View) {
        for(i in 1..5) {
            val title = et_title.text.toString() + ":" + i
            val message = et_message.text.toString() + ":" + i
            val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .build()

            // id 為後續需要刪除通知需要給的資訊
            notificationManager.notify(i, notification)
        }
    }

    // 根據相同 ID 重新發送以覆蓋
    fun updateFromChannel1(view: View) {
        val id = 1001
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val title = et_title.text.toString()
        val message = et_message.text.toString()
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setOngoing(true)   // 無法刪除
            .setContentText(message)
            .setContentIntent(pIntent)
            .setSubText("continue...")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        // id 為後續需要刪除通知需要給的資訊
        notificationManager.notify(id, notification)
    }
}
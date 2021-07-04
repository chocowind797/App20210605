package com.study.app_tictactoe_firebase

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

// 要在 AndroidManifest.xml 新增 android:name=".App"
class App: Application() {

    companion object {
        val CHANNEL_1_ID = "channel1"
        val CHANNEL_2_ID = "channel2"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    fun createNotificationChannels() {
        // 判斷 android 版本(Android 8.0 以上才有支援 channel(通知分級), 7.0 以下沒有分級)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel_1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel_1.description = "This is channel 1"

            val channel_2 = NotificationChannel(
                CHANNEL_2_ID,
                "Channel 2",
                NotificationManager.IMPORTANCE_LOW
            )
            channel_2.description = "This is channel 2"

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel_1)
            manager.createNotificationChannel(channel_2)
        }
    }
}
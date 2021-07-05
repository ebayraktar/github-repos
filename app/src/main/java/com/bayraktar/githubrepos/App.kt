package com.bayraktar.githubrepos

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    //We can send notifications
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = getString(R.string.notification_channel)
            val manager = getSystemService(
                NotificationManager::class.java
            )!!
            manager.createNotificationChannel(channel1)
        }
    }

    companion object {
        const val CHANNEL_1_ID = "Channel1"
    }
}
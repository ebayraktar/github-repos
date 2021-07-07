package com.bayraktar.githubrepos

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bayraktar.githubrepos.network.GitHubRepository
import com.bayraktar.githubrepos.network.ServiceBuilder
import com.bayraktar.githubrepos.room.FavoriteRepository
import com.bayraktar.githubrepos.room.FavoriteRoomDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { FavoriteRoomDb.getDatabase(this, applicationScope) }
    val repository by lazy { FavoriteRepository(database.favoriteDao()) }
    val gitHubRepository by lazy { GitHubRepository(ServiceBuilder.buildService()) }
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
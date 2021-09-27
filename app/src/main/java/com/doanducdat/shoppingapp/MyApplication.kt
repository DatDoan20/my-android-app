package com.doanducdat.shoppingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.HiltAndroidApp

/***
 * this class use init Hilt and utilize to create channel notification app
 */

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppConstants.NewNotifyComment.NAME
            val descriptionText = AppConstants.NewNotifyComment.DES
            val importance = AppConstants.NewNotifyComment.IMPORTANT
            val channel =
                NotificationChannel(AppConstants.NewNotifyComment.CHANNEL, name, importance).apply {
                    description = descriptionText
                }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
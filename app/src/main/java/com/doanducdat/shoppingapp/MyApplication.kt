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
    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelComment()
        createNotificationChannelOrder()
    }

    private fun createNotificationChannelComment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppConstants.NewNotifyComment.NAME
            val descriptionText = AppConstants.NewNotifyComment.DES
            val importance = AppConstants.NewNotifyComment.IMPORTANT
            val channel =
                NotificationChannel(AppConstants.NewNotifyComment.CHANNEL, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel comment with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelOrder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppConstants.StateNotifyOrder.NAME
            val descriptionText = AppConstants.StateNotifyOrder.DES
            val importance = AppConstants.StateNotifyOrder.IMPORTANT
            val channel =
                NotificationChannel(AppConstants.StateNotifyOrder.CHANNEL, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel order with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}
package com.doanducdat.shoppingapp.utils

import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.module.review.NotifyComment

object HandlerNotification {

    fun sendNotification(context: Context, notifyComment: NotifyComment) {
        val remoteViews: RemoteViews =
            RemoteViews(context.packageName, R.layout.layout_notify_msg_generic)
        with(remoteViews) {
            setTextViewText(R.id.txt_name_notify, notifyComment.commentId.userId.name)
            setTextViewText(R.id.txt_content_notify, notifyComment.commentId.comment)
        }
        val notification =
            NotificationCompat.Builder(context, AppConstants.NewNotifyComment.CHANNEL)
                .setSmallIcon(R.drawable.ic_notifications)
//                .setCustomContentView(remoteViews)
                .setContentTitle(notifyComment.commentId.userId.name + " vừa bình luận")
                .setContentText(notifyComment.commentId.comment)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        val notifyManager = NotificationManagerCompat.from(context)
        notifyManager.notify(notifyComment.createdAt.time.toInt(), notification)
    }
}
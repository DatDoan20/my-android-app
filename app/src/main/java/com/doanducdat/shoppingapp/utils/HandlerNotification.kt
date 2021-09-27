package com.doanducdat.shoppingapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.module.review.NotifyComment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object HandlerNotification {

    suspend fun sendNotification(context: Context, notifyComment: NotifyComment) {
        val notification =
            NotificationCompat.Builder(context, AppConstants.NewNotifyComment.CHANNEL)
                .setSmallIcon(R.drawable.ic_notifications)
                .setCustomContentView(initRemoteView(context, notifyComment))
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        val notifyManager = NotificationManagerCompat.from(context)
        notifyManager.notify(notifyComment.createdAt.time.toInt(), notification)
    }

    private suspend fun initRemoteView(
        context: Context,
        notifyComment: NotifyComment
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.layout_notify_msg_generic)
        with(remoteViews) {
            setTextViewText(R.id.txt_name_notify, notifyComment.commentId.userId.name)
            setTextViewText(R.id.txt_content_notify, notifyComment.commentId.comment)
        }
        val bitmap = loadAvatarUserNotify(context, notifyComment.commentId.userId.getUrlAvatar())
        remoteViews.setImageViewBitmap(R.id.img_avatar_notify, bitmap)
        return remoteViews
    }

    private suspend fun loadAvatarUserNotify(context: Context, url: String): Bitmap? {
        var bitmap: Bitmap? = null
        val job = CoroutineScope(Dispatchers.IO).launch {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .transformations(CircleCropTransformation())
                .build()
            val result = loader.execute(request).drawable
            bitmap = (result as BitmapDrawable).bitmap
        }
        job.join()
        return bitmap
    }
}
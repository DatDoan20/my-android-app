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
import com.doanducdat.shoppingapp.module.order.NotifyOrder
import com.doanducdat.shoppingapp.module.review.NotifyComment
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HandlerNotification(val context: Context) {

    private val notifyManager = NotificationManagerCompat.from(context)
    private val date: Date = Date()

    suspend fun sendStateOrder(notifyOrder: NotifyOrder) {
        val content: String = FormValidation.getContentStateOrder(notifyOrder) ?: return
        val viewOderCollapse = getRemoteView(
            notifyOrder.senderName,
            content,
            notifyOrder.getUrlReceiverAvatar(),
            R.layout.layout_notify_msg_collap_generic
        )
        val viewOrderExpand = getRemoteView(
            notifyOrder.senderName,
            content,
            notifyOrder.getUrlReceiverAvatar(),
            R.layout.layout_notify_msg_expand_generic
        )
        buildNotification(
            viewOderCollapse,
            viewOrderExpand,
            AppConstants.StateNotifyOrder.CHANNEL,
            date.time.toInt()
        )
    }

    suspend fun sendNewComment(notifyComment: NotifyComment) {
        val viewCommentCollapse = getRemoteView(
            notifyComment.commentId.userId.name, notifyComment.commentId.comment,
            notifyComment.commentId.userId.getUrlAvatar(), R.layout.layout_notify_msg_collap_generic
        )
        val viewCommentExpand = getRemoteView(
            notifyComment.commentId.userId.name, notifyComment.commentId.comment,
            notifyComment.commentId.userId.getUrlAvatar(), R.layout.layout_notify_msg_expand_generic
        )
        buildNotification(
            viewCommentCollapse,
            viewCommentExpand,
            AppConstants.NewNotifyComment.CHANNEL,
            notifyComment.createdAt.time.toInt()
        )
    }

    private suspend fun getRemoteView(
        name: String,
        content: String,
        urlAvatar: String,
        layoutId: Int
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, layoutId)
        remoteViews.setTextViewText(R.id.txt_name_notify, name)
        remoteViews.setTextViewText(R.id.txt_content_notify, content)

        //load and make circle avatar
        val bitmap = loadAvatarUserNotify(urlAvatar)

        remoteViews.setImageViewBitmap(R.id.img_avatar_notify, bitmap)
        return remoteViews
    }

    private suspend fun loadAvatarUserNotify(url: String): Bitmap? {
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

    private fun buildNotification(
        remoteViewCollapse: RemoteViews,
        remoteViewExpand: RemoteViews,
        channel: String,
        notifyId: Int
    ) {
        val notification =
            NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_notifications)
                .setCustomContentView(remoteViewCollapse)
                .setCustomBigContentView(remoteViewExpand)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        notifyManager.notify(notifyId, notification)
    }
}
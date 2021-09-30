package com.doanducdat.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemNotificationBinding
import com.doanducdat.shoppingapp.module.review.NotifyComment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.HandlerTime
import com.doanducdat.shoppingapp.utils.MyBgCustom
import com.doanducdat.shoppingapp.utils.validation.FormValidation

class NotifyCommentPagingAdapter :
    PagingDataAdapter<NotifyComment, NotifyCommentPagingAdapter.NotifyCommentPagingViewHolder>(
        PRODUCT_COMPARATOR
    ) {
    val myBgCustom = MyBgCustom.getInstance()
//    var callbackClickRepComment: (comment: Comment) -> Unit = {}
//
//    fun mySetOnClickRepComment(callbackFun: (comment: Comment) -> Unit) {
//        callbackClickRepComment = callbackFun
//    }

    inner class NotifyCommentPagingViewHolder(
        val binding: ItemNotificationBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notifyComment: NotifyComment?) {
            if (notifyComment == null) return
            with(binding) {
                imgAvatar.load(notifyComment.commentId.userId.getUrlAvatar())
                txtName.text = notifyComment.commentId.userId.name
                txtTimeComment.text =
                    HandlerTime.getTimeAgo(notifyComment.updatedAt.time)
                txtContentNotify.text = notifyComment.commentId.comment
                if (!notifyComment.receiverIds[0].readState) {
                    imgBlueDotReadState.visibility = View.VISIBLE
                    imgBlueDotReadState.background =
                        myBgCustom.bgOval(AppConstants.ColorHex.UN_READ_STATE_NOTIFY)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: NotifyCommentPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotifyCommentPagingViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifyCommentPagingViewHolder(binding, parent.context)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<NotifyComment>() {
            override fun areItemsTheSame(oldItem: NotifyComment, newItem: NotifyComment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotifyComment,
                newItem: NotifyComment
            ): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}
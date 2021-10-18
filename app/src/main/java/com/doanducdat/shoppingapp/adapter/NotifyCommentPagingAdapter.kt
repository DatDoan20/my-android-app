package com.doanducdat.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemNotificationBinding
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.handler.HandlerTime
import com.doanducdat.shoppingapp.utils.handler.HandlerViewState
import com.doanducdat.shoppingapp.utils.validation.FormValidation

class NotifyCommentPagingAdapter(
    context: Context,
) : PagingDataAdapter<NotifyComment, NotifyCommentPagingAdapter.NotifyCommentPagingViewHolder>(
    PRODUCT_COMPARATOR
) {
    val handlerViewState: HandlerViewState = HandlerViewState(context)
    private var callbackClickOpenMenu: (
        notifyComment: NotifyComment, itemBinding: ItemNotificationBinding
    ) -> Unit = { _: NotifyComment, _: ItemNotificationBinding -> }

    fun mySetOnClickRead(
        funClickOpenMenu: (
            notifyComment: NotifyComment, itemBinding: ItemNotificationBinding
        ) -> Unit
    ) {
        callbackClickOpenMenu = funClickOpenMenu
    }

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
                txtTimeComment.text = HandlerTime.getTimeAgo(notifyComment.updatedAt.time)
                txtContentNotify.text =
                    FormValidation.getContentComment(notifyComment.commentId.comment)
                checkViewReadOrUnRead(notifyComment)
                setUpActionClick(notifyComment)
            }
        }

        private fun checkViewReadOrUnRead(notifyComment: NotifyComment) {
            with(binding) {
                // time read all < time notify -> UnRead All -> check particular state Read? in notify
                if (InfoLocalUser.currentUser?.readAllCommentNoti?.before(notifyComment.updatedAt) == true) {

                    // if readState == true -> Read -> setState..., otherwise default view is unRead
                    if (notifyComment.receiverIds[0].readState) { //-> read
                        handlerViewState.setStateReadDot(imgBlueDotReadState)
                        handlerViewState.setColorReadTextView(txtName, txtTimeComment, txtContentNotify)
                        return
                    }
                    //->UnRead: view is default UnRead not need handle here
                } else {
                    handlerViewState.setStateReadDot(imgBlueDotReadState)
                    handlerViewState.setColorReadTextView(txtName, txtTimeComment, txtContentNotify)
                }
            }
        }

        private fun setUpActionClick(notifyComment: NotifyComment) {
            with(binding) {
                imgMore.setOnClickListener {
                    openPopupMenu(notifyComment)
                }
                layoutItemNotification.setOnClickListener {
                    openPopupMenu(notifyComment)
                }
            }
        }

        private fun openPopupMenu(notifyComment: NotifyComment) {
            callbackClickOpenMenu.invoke(notifyComment, binding)
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
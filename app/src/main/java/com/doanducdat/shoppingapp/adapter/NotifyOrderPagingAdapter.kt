package com.doanducdat.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemNotificationBinding
import com.doanducdat.shoppingapp.module.order.NotifyOrder
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.HandlerTime
import com.doanducdat.shoppingapp.utils.MyBgCustom
import com.doanducdat.shoppingapp.utils.validation.FormValidation

class NotifyOrderPagingAdapter :
    PagingDataAdapter<NotifyOrder, NotifyOrderPagingAdapter.NotifyOrderPagingViewHolder>(
        PRODUCT_COMPARATOR
    ) {
    val myBgCustom = MyBgCustom.getInstance()
//    var callbackClickRepComment: (comment: Comment) -> Unit = {}
//
//    fun mySetOnClickRepComment(callbackFun: (comment: Comment) -> Unit) {
//        callbackClickRepComment = callbackFun
//    }

    inner class NotifyOrderPagingViewHolder(
        val binding: ItemNotificationBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notifyOrder: NotifyOrder?) {
            if (notifyOrder == null) return
            with(binding) {
                txtName.text = AppConstants.StateNotifyOrder.RECEIVER_NAME

                imgAvatar.setImageResource(R.drawable.ic_order_notification_white)
                txtTimeComment.text = HandlerTime.getTimeAgo(notifyOrder.updatedAt.time)
                txtContentNotify.text = FormValidation.getContentStateOrder(
                    notifyOrder.orderId.totalPayment,
                    notifyOrder.orderId.state
                )

                if (!notifyOrder.receiverIds[0].readState) {
                    imgBlueDotReadState.visibility = View.VISIBLE
                    imgBlueDotReadState.background =
                        myBgCustom.bgOval(AppConstants.ColorHex.UN_READ_STATE_NOTIFY)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: NotifyOrderPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotifyOrderPagingViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifyOrderPagingViewHolder(binding, parent.context)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<NotifyOrder>() {
            override fun areItemsTheSame(oldItem: NotifyOrder, newItem: NotifyOrder): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotifyOrder,
                newItem: NotifyOrder
            ): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}
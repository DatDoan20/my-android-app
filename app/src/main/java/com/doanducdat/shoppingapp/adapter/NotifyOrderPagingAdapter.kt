package com.doanducdat.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemNotificationBinding
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.HandlerTime
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import com.doanducdat.shoppingapp.utils.validation.ViewState

class NotifyOrderPagingAdapter(
    context: Context,
) : PagingDataAdapter<NotifyOrder, NotifyOrderPagingAdapter.NotifyOrderPagingViewHolder>(
    PRODUCT_COMPARATOR
) {
    val viewState = ViewState(context)
    private var callbackClickOpenMenu: (
        notifyOrder: NotifyOrder, positionItem: Int, itemBinding: ItemNotificationBinding
    ) -> Unit = { _: NotifyOrder, _: Int, _: ItemNotificationBinding -> }

    fun mySetOnClickRead(
        funClickOpenMenu: (
            notifyOrder: NotifyOrder, positionItem: Int, itemBinding: ItemNotificationBinding
        ) -> Unit
    ) {
        callbackClickOpenMenu = funClickOpenMenu
    }

    inner class NotifyOrderPagingViewHolder(
        val binding: ItemNotificationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notifyOrder: NotifyOrder?, positionItem: Int) {
            if (notifyOrder == null) return

            with(binding) {
                imgAvatar.setImageResource(R.drawable.ic_order_notification_blue)
                imgAvatar.borderWidth = 0

                txtName.text = AppConstants.StateNotifyOrder.RECEIVER_NAME
                txtTimeComment.text = HandlerTime.getTimeAgo(notifyOrder.updatedAt.time)
                txtContentNotify.text =
                    FormValidation.getContentStateOrder(notifyOrder.totalPayment, notifyOrder.state)

                checkViewReadOrUnRead(notifyOrder)
                setUpActionClick(notifyOrder, positionItem)
            }
        }

        private fun checkViewReadOrUnRead(notifyOrder: NotifyOrder) {
            with(binding) {
                // time read all < time notify -> UnRead All -> check particular state Read? in notify
                if (InfoUser.currentUser?.readAllOrderNoti?.before(notifyOrder.updatedAt) == true) {

                    // if readState == true -> Read -> setState..., otherwise default view is unRead
                    if (notifyOrder.receiverIds[0].readState) { //-> read
                        viewState.setStateReadDot(imgBlueDotReadState)
                        viewState.setColorReadTextView(txtName, txtTimeComment, txtContentNotify)
                        return
                    }
                    //->UnRead: view is default UnRead not need handle here
                } else {
                    viewState.setStateReadDot(imgBlueDotReadState)
                    viewState.setColorReadTextView(txtName, txtTimeComment, txtContentNotify)
                }
            }
        }

        private fun setUpActionClick(notifyOrder: NotifyOrder, positionItem: Int) {
            with(binding) {
                imgMore.setOnClickListener {
                    openPopupMenu(notifyOrder, positionItem)
                }
                layoutItemNotification.setOnClickListener {
                    openPopupMenu(notifyOrder, positionItem)
                }
            }
        }

        private fun openPopupMenu(notifyOrder: NotifyOrder, positionItem: Int) {
            callbackClickOpenMenu.invoke(notifyOrder, positionItem, binding)
        }
    }

    override fun onBindViewHolder(holder: NotifyOrderPagingViewHolder, position: Int) {
        holder.bind(getItem(position), holder.absoluteAdapterPosition)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotifyOrderPagingViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifyOrderPagingViewHolder(binding)
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
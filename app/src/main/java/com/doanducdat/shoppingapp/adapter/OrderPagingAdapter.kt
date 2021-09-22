package com.doanducdat.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemOrderBinding
import com.doanducdat.shoppingapp.module.order.Order
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.validation.FormValidation

class OrderPagingAdapter :
    PagingDataAdapter<Order, OrderPagingAdapter.OrderPagingViewHolder>(PRODUCT_COMPARATOR) {

    private var callbackClickViewDetail: (order: Order) -> Unit = {}
    private var callbackClickCancel: (order: Order) -> Unit = {}

    fun mySetOnClickViewDetail(callbackFun: (order: Order) -> Unit) {
        callbackClickViewDetail = callbackFun
    }

    fun mySetOnClickCancelOrder(callbackFun: (order: Order) -> Unit) {
        callbackClickCancel = callbackFun
    }

    inner class OrderPagingViewHolder(val binding: ItemOrderBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order?) {
            if (order != null) {
                val sizePurchasedProduct = order.purchasedProducts.size
                binding.txtNameProduct.text = getNameFirstProduct(order, sizePurchasedProduct)
                binding.txtNameReceiver.text = order.nameUser
                binding.txtDateOrder.text =
                    order.createdAt?.let { FormValidation.formatDay(it, context) }
                binding.txtAddress.text = order.addressDelivery
                binding.txtNumberProduct.text = sizePurchasedProduct.toString()
                binding.txtTotalPayment.text = FormValidation.formatMoney(order.totalPayment)
                setStateOrder(order.state)
                setActionOrder(order)
            }
        }

        private fun getNameFirstProduct(order: Order, sizePurchasedProduct: Int): String {
            var nameFirstProduct = order.purchasedProducts[0].name
            //many product -> custom nameFirstProduct to display
            if (sizePurchasedProduct > 1) {
                val suffixes = " và $sizePurchasedProduct mặt hàng khác..."
                if (nameFirstProduct.length > 45) {
                    nameFirstProduct.substring(0, 44) + suffixes
                } else {
                    nameFirstProduct += suffixes
                }
            }
            return nameFirstProduct
        }

        private fun setStateOrder(state: String) {
            var msgState = ""
            var nameDrawable = 0
            when (state) {
                AppConstants.Order.WAITING -> {
                    msgState = AppConstants.Order.MSG_WAITING
                    nameDrawable = R.drawable.bg_state_order_waiting
                }
                AppConstants.Order.ACCEPTED -> {
                    msgState = AppConstants.Order.ACCEPTED
                    nameDrawable = R.drawable.bg_state_order_accepted
                }
                AppConstants.Order.CANCELED -> {
                    msgState = AppConstants.Order.MSG_CANCELED
                    nameDrawable = R.drawable.bg_state_order_canceled
                }
            }
            binding.txtState.background = ContextCompat.getDrawable(context, nameDrawable)
            binding.txtState.text = msgState
        }

        private fun setActionOrder(order: Order) {
            binding.btnViewDetailOrder.setOnClickListener {
                callbackClickViewDetail(order)
            }
            if (order.state == AppConstants.Order.ACCEPTED) {
                binding.btnCancelOrder.visibility = View.GONE
            }
            if (order.state == AppConstants.Order.WAITING) {
                binding.btnCancelOrder.setOnClickListener {
                    callbackClickCancel(order)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OrderPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderPagingViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderPagingViewHolder(binding, parent.context)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}
package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.databinding.ItemOrderBinding
import com.doanducdat.shoppingapp.module.order.Order
import com.doanducdat.shoppingapp.utils.validation.FormValidation

class OrderPagingAdapter :
    PagingDataAdapter<Order, OrderPagingAdapter.OrderPagingViewHolder>(PRODUCT_COMPARATOR) {

    var callbackClickOrder: (order: Order) -> Unit = {}

    fun mySetOnClickOder(callbackFun: (order: Order) -> Unit) {
        callbackClickOrder = callbackFun
    }

    inner class OrderPagingViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order?) {
            if (order != null) {
                var nameFirstProduct = order.purchasedProducts[0].name
                val sizePurchasedProduct = order.purchasedProducts.size
                val suffixes = " và $sizePurchasedProduct mặt hàng khác..."
                if (nameFirstProduct.length > 45) {
                    nameFirstProduct = nameFirstProduct.substring(0, 44) + suffixes
                } else {
                    nameFirstProduct += suffixes
                }
                binding.txtNameProduct.text = nameFirstProduct
                binding.txtNameReceiver.text = order.nameUser
                binding.txtDateOrder.text = order.createdAt?.let { FormValidation.formatDay(it) }
                binding.txtNumberProduct.text = sizePurchasedProduct.toString()
                binding.txtTotalPayment.text = order.totalPayment.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: OrderPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderPagingViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderPagingViewHolder(binding)
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
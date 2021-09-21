package com.doanducdat.shoppingapp.module.order

import com.google.gson.annotations.SerializedName

/**
 * state, payment have value default, can null when post
 */
class Order(
    @SerializedName("_id")
    val id: String?,
    val addressDelivery: String,
    val costDelivery: Int,
    val emailUser: String,
    val nameUser: String,
    val paymentMode: String,
    val phoneUser: String,
    val state: String,
    val totalPayment: Int,
    val totalPrice: Int,
    val purchasedProducts: List<PurchasedProduct>,
    val createdAt: String?
)
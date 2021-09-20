package com.doanducdat.shoppingapp.module.order

/**
 * state, payment have value default, can null when post
 */
data class Order(
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
)
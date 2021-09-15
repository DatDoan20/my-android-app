package com.doanducdat.shoppingapp.module.cart

/**
 * infoProduct is IdProduct and this data class use to send request
 *
 * add to cart
 */
data class Cart(
    val color: String,
    val finalPrice: Int,
    val infoProduct: String,
    val quantity: Int,
    val size: String
)
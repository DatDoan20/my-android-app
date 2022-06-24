package com.doanducdat.shoppingapp.model.cart

/**
 * infoProduct is IdProduct and this data class use to send request
 *
 * add to cart
 */
data class Cart(
    val color: String,
    val price: Int,
    val infoProduct: String,
    val quantity: Int,
    val size: String,
    val id:String? = null,
)
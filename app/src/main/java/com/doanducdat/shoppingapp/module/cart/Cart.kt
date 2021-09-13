package com.doanducdat.shoppingapp.module.cart

/**
 * infoProduct is IdProduct
 */
data class Cart(
    val color: String,
    val finalPrice: Int,
    val infoProduct: String,
    val quantity: Int,
    val size: String
)
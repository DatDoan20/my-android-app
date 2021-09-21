package com.doanducdat.shoppingapp.module.order

/**
 * "finalPrice in here" is "price in cart" (when number increase,price increase as well)
 *
 * -> price * quantify
 *
 * "price in here" is price of one product item -> "price in cart" / "quantity in cart"
 */
data class PurchasedProduct(
    val color: String,
    val discount: Int,
    val finalPrice: Int,
    val imageCover: String,
    val name: String,
    val price: Int,
    val productId: String,
    val quantity: Int,
    val size: String
)
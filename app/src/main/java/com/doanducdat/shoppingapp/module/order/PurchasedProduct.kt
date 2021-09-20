package com.doanducdat.shoppingapp.module.order

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
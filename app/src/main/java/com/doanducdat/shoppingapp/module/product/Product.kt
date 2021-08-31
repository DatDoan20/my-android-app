package com.doanducdat.shoppingapp.module.product

import java.text.DecimalFormat

class Product(
    val name: String,
    val description: String,
    val images: List<String>,
    val imageCover: String,
    val slug: String,
    val price: Int,
    val brand: String,
    val size: String,
    val color: String,
    val material: String,
    val pattern: String,
    val discount: Int,
    val outOfStock: Boolean,
    val type: String,
    val category: String,
    val ratingsAverage: Float,
    val ratingsQuantity: Int
) {
    fun getPrice(): String? {
        val dec = DecimalFormat("#,###")
        return dec.format(this.price)
    }

    fun getDiscount(): String {
        return "$discount%"
    }
}
package com.doanducdat.shoppingapp.module.cart

import com.doanducdat.shoppingapp.module.product.Product

/**
 * infoProduct is IdProduct
 */
data class PopulatedCart(
    val color: String,
    val finalPrice: Int,
    val infoProduct: Product,
    val quantity: Int,
    val size: String
)
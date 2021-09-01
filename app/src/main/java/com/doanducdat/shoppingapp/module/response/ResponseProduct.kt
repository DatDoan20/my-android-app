package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.product.Product

class ResponseProduct(
    val status: String,
    val data: List<Product>,
    val error: String,
    val message: String
) {
}
package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.product.Product

class ResponseProduct(
    val status: String,
    val data: List<Product>,
    val error: String,
    val message: String
) {
}
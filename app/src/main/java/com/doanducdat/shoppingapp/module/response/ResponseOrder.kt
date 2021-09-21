package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.order.Order

class ResponseOrder(
    val status: String,
    val data: List<Order>,
    val error: String,
    val message: String
) {
}
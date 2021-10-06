package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.order.Order

class ResponseOrder(
    val status: String,
    val data: List<Order>,
    val error: String,
    val message: String
) {
}
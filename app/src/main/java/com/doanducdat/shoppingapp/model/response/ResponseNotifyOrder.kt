package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.order.NotifyOrder

class ResponseNotifyOrder(
    val status: String,
    val data: List<NotifyOrder>,
    val error: String,
    val message: String
) {
}
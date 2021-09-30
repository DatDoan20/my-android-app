package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.order.NotifyOrder

class ResponseNotifyOrder(
    val status: String,
    val data: List<NotifyOrder>,
    val error: String,
    val message: String
) {
}
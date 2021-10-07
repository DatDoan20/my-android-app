package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.order.Order

/**
 * server return totalPayment, addressDelivery in data
 */
class ResponseOrderPost(
    val status: String,
    val data: Order,
    val error: String,
    val message: String
) {
}
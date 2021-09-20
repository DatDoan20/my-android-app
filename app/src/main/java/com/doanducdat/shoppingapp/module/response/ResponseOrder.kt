package com.doanducdat.shoppingapp.module.response

class ResponseOrder(
    val status: String,
    val data: Order,
    val error: String,
    val message: String
) {
}
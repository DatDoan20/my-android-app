package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.user.User

class ResponseHandleProductInCart(
    val status: String,
    val data: User,
    val error: String,
    val message: String
) {
}
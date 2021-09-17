package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.user.User

class ResponseUser(
    val status: String,
    val data:User,
    val error: String,
    val message: String
) {
}
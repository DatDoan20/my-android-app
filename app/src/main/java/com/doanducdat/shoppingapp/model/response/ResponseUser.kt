package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.user.User

class ResponseUser(
    val status: String,
    val data:User,
    val error: String,
    val message: String
) {
}
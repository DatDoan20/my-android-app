package com.doanducdat.shoppingapp.utils

import com.doanducdat.shoppingapp.module.user.User

object InfoUser {
    var currentUser: User? = null
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
}
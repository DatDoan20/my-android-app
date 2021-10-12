package com.doanducdat.shoppingapp.utils

import com.doanducdat.shoppingapp.model.user.User

object InfoLocalUser {
    var currentUser: User? = null
    var localToken: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
}
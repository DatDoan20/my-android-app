package com.doanducdat.shoppingapp.utils

import com.doanducdat.shoppingapp.model.user.User

object InfoUser {
    var currentUser: User? = null
    var numberUnReadNotifyComment: Int? = null
    var numberUnReadNotifyOrder: Int? = null
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
}
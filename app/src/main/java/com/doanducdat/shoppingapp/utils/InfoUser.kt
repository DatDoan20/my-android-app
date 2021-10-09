package com.doanducdat.shoppingapp.utils

import com.doanducdat.shoppingapp.model.user.User

object InfoUser {
    var currentUser: User? = null
    var numberUnReadNotifyComment: Int = 0
    var numberUnReadNotifyOrder: Int = 0
    var localToken: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
}
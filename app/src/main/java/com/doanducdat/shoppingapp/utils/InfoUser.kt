package com.doanducdat.shoppingapp.utils

import com.doanducdat.shoppingapp.module.user.User

object InfoUser {
    var currentUser: User? = null
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
        .append("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTJiODdlMDU3YzEwMjMyMDBkMDk3YzYiLCJpYXQiOjE2MzE4NTkzNzEsImV4cCI6MTYzMjQ2NDE3MX0.ENF4u-MKw_jKaipjFAJa2laXXrwOLX4YMhntjEq-gIU")
}
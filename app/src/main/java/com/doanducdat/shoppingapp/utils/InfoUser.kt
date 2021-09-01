package com.doanducdat.shoppingapp.utils

object InfoUser {
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
        .append("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTJiODdlMDU3YzEwMjMyMDBkMDk3YzYiLCJpYXQiOjE2MzA0NjU3MDUsImV4cCI6MTYzMDU1MjEwNX0.k6MmROvBgbHYNE0gbeoW49XlJoyfP03k0Gw-8gic4-E")
}
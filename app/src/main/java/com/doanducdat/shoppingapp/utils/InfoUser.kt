package com.doanducdat.shoppingapp.utils

object InfoUser {
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
        .append("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTJiODdlMDU3YzEwMjMyMDBkMDk3YzYiLCJpYXQiOjE2MzA4MTgxMTUsImV4cCI6MTYzMDkwNDUxNX0.1sFxuV7hXH_UHvoIiPZkw4htRSRaKgDe8QeFT4UkuOc")
}
package com.doanducdat.shoppingapp.utils

object InfoUser {
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
        .append("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTJiODdlMDU3YzEwMjMyMDBkMDk3YzYiLCJpYXQiOjE2MzA5MDUwNDgsImV4cCI6MTYzMDk5MTQ0OH0.vLSk8s3boXZDremmSQKm45wwiFIqizmNDskG3yR1Tvo")
}
package com.doanducdat.shoppingapp.utils

object InfoUser {
    var token: StringBuffer = StringBuffer(AppConstants.HeaderRequest.BEARER)
        .append(" ")
        .append("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTJiODdlMDU3YzEwMjMyMDBkMDk3YzYiLCJpYXQiOjE2MzExNzM1NjEsImV4cCI6MTYzMTc3ODM2MX0.KQogTAeRu223h0SRBxqm8ZrgKEtQ-UhdlB9U5Z09hJ0")
}
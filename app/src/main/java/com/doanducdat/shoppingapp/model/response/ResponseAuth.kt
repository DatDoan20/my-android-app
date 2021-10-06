package com.doanducdat.shoppingapp.model.response

import com.google.gson.annotations.SerializedName

class ResponseAuth(
    val status: String,
    @SerializedName("data")
    val token: String,
    val error: String,
    val message: String
) {
}
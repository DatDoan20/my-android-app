package com.doanducdat.shoppingapp.module.user

import com.doanducdat.shoppingapp.module.cart.Cart
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    val avatar: String,
    val birthYear: String,
    val cart: List<Cart>,
    val createdAt: String,
    val email: String,
    val favProducts: List<Any>,
    val name: String,
    val phone: String,
    val role: String,
    val sex: String,
    val stateVerifyEmail: Boolean,
    val updatedAt: String
)
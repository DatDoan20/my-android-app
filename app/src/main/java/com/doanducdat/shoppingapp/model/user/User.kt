package com.doanducdat.shoppingapp.model.user

import com.doanducdat.shoppingapp.model.cart.PopulatedCart
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    val avatar: String,
    val birthYear: String,
    val cart: MutableList<PopulatedCart>,
    val createdAt: String,
    var email: String,
    val favProducts: List<Any>,
    val name: String,
    val phone: String,
    val role: String,
    val sex: String,
    var stateVerifyEmail: Boolean,
    val updatedAt: String
) {
    fun getUrlAvatar(): String {
        //avatar is name of image, not link
        // thi fun return URL of avatar
        return "${AppConstants.Server.HOST}${AppConstants.LinkImg.USER}${avatar}"
    }
}
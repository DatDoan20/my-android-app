package com.doanducdat.shoppingapp.model.user

import com.doanducdat.shoppingapp.model.cart.PopulatedCart
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    @SerializedName("_id")
    val id: String,
    var avatar: String,
    var birthYear: String,
    val cart: MutableList<PopulatedCart>,
    val createdAt: String,
    var email: String,
    val favProducts: List<Any>,
    var name: String,
    val phone: String,
    val role: String,
    var sex: String,
    var stateVerifyEmail: Boolean,
    val updatedAt: String,
    var readAllOrderNoti:Date,
    var readAllCommentNoti:Date
) {
    fun getUrlAvatar(): String {
        //avatar is name of image, not link
        // thi fun return URL of avatar
        return "${AppConstants.Server.PUBLIC_HOST}${AppConstants.LinkImg.USER}${avatar}"
    }
}
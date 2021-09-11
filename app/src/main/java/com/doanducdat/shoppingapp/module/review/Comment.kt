package com.doanducdat.shoppingapp.module.review

import com.doanducdat.shoppingapp.module.user.User
import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("_id")
    val id: String,
    val comment: String,
    val reviewId: String,
    val updatedAt: String,
    val createdAt: String,
    val userId: User
)
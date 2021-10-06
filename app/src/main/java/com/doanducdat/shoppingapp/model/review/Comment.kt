package com.doanducdat.shoppingapp.model.review

import com.doanducdat.shoppingapp.model.user.User
import com.google.gson.annotations.SerializedName
import java.util.*


/**
 * show comment latest -> use updatedAt
 */
data class Comment(
    @SerializedName("_id")
    val id: String,
    val comment: String,
    val reviewId: String,
    val updatedAt: Date,
    val createdAt: Date,
    val userId: User
)
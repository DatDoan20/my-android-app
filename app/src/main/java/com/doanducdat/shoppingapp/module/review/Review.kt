package com.doanducdat.shoppingapp.module.review

import com.doanducdat.shoppingapp.module.user.User
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * only those who bought product can review, and review can not be edited
 *
 * -> use createdAt
 */
data class Review(
    @SerializedName("_id")
    val id: String,
    val comments: List<Comment>,
    val createdAt: String,
    val images: List<String>,
    val productId: String,
    val rating: Int,
    val review: String,
    val updatedAt: Date,
    val userId: User
)
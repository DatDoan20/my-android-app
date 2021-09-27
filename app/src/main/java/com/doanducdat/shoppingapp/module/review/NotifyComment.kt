package com.doanducdat.shoppingapp.module.review

import com.google.gson.annotations.SerializedName
import java.util.*

class NotifyComment(
    @SerializedName("_id")
    val id: String,
    val commentId: Comment,
    val updatedAt: Date,
    val createdAt: Date,
) {}
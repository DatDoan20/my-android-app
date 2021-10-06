package com.doanducdat.shoppingapp.model.review

import com.google.gson.annotations.SerializedName
import java.util.*

class NotifyComment(
    @SerializedName("_id")
    val id: String,
    val commentId: Comment,
    val updatedAt: Date,
    val createdAt: Date,
    val receiverIds: List<Receiver>
) {}
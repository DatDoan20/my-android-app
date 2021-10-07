package com.doanducdat.shoppingapp.model.order

import com.doanducdat.shoppingapp.model.review.Receiver
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * notifyOrder is not NotifyOrder stored in DB
 */
class NotifyOrder(
    //here is notifyOrder in NotifyOder Database
    @SerializedName("_id")
    val id: String,
    val updatedAt: Date,
    val receiverIds: List<Receiver>,
    val state: String,
    val totalPayment: Int,
) {
}
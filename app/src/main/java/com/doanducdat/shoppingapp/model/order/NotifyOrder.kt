package com.doanducdat.shoppingapp.model.order

import com.doanducdat.shoppingapp.model.review.Receiver
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * notifyOrder is not NotifyOrder stored in DB, NotifyOrder In DB to notify with Admin
 *
 * NotifyOrder here is simple notification to notify about state order when admin accept/cancel order
 */
class NotifyOrder(
    //here is notifyOrder in NotifyOder Database
    @SerializedName("_id")
    val id:String,
    val updatedAt: Date,
    val orderId:Order,
    val receiverIds: List<Receiver>,
    // here is simple property of emitted notification when admin update state order
    val state: String,
    val totalPayment: Int,
) {
}
package com.doanducdat.shoppingapp.module.order

import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * notifyOrder is not NotifyOrder stored in DB, NotifyOrder In DB to notify with Admin
 *
 * NotifyOrder here is simple notification to notify about state order when admin accept/cancel order
 */
class NotifyOrder(
    @SerializedName("_id")
    val id:String,
    val state: String,
    val totalPayment: Int,
    val senderName: String,
    val createdAt: Date,
    private val senderAvatar: String,
) {
    fun getUrlReceiverAvatar(): String {
        return "${AppConstants.Server.HOST}${AppConstants.LinkImg.USER}${senderAvatar}"
    }
}
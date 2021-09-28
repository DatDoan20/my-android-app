package com.doanducdat.shoppingapp.module.order

import com.doanducdat.shoppingapp.utils.AppConstants

/**
 * notifyOrder is not NotifyOrder stored in DB, NotifyOrder In DB to notify with Admin
 *
 * NotifyOrder here is simple notification to notify about state order when admin accept/cancel order
 */
class NotifyOrder(
    val state: String,
    val totalPayment: Int,
    val senderName: String,
    private val senderAvatar: String,
) {
    fun getUrlReceiverAvatar(): String {
        return "${AppConstants.Server.HOST}${AppConstants.LinkImg.USER}${senderAvatar}"
    }
}
package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.review.NotifyComment

class ResponseNotifyComment(
    val status: String,
    val data: List<NotifyComment>,
    val error: String,
    val message: String
) {
}
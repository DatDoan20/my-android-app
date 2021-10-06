package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.review.NotifyComment

class ResponseNotifyComment(
    val status: String,
    val data: List<NotifyComment>,
    val error: String,
    val message: String
) {
}
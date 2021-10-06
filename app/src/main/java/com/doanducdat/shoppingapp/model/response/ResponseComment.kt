package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.review.Comment

class ResponseComment(
    val status: String,
    val data: List<Comment>,
    val error: String,
    val message: String
) {
}
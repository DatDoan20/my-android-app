package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.review.Comment

class ResponseComment(
    val status: String,
    val data: List<Comment>,
    val error: String,
    val message: String
) {
}
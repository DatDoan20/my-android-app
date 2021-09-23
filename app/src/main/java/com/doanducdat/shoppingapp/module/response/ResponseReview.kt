package com.doanducdat.shoppingapp.module.response

import com.doanducdat.shoppingapp.module.review.Review

class ResponseReview(
    val status: String,
    val data: List<Review>,
    val error: String,
    val message: String
) {
}
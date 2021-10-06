package com.doanducdat.shoppingapp.model.response

import com.doanducdat.shoppingapp.model.review.Review

class ResponseReview(
    val status: String,
    val data: List<Review>,
    val error: String,
    val message: String
) {
}
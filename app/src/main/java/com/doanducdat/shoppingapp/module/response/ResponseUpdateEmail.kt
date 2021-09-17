package com.doanducdat.shoppingapp.module.response

/**
 * data is email was updated, it was responded from server
 */
class ResponseUpdateEmail(
    val status: String,
    val error: String,
    val data:String,
    val message: String
) {
}
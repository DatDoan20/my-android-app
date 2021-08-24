package com.doanducdat.shoppingapp.module

import java.util.*

class Response(
    val status: String,
    val data: List<Objects>,
    val error: String,
    val message: String
) {
}
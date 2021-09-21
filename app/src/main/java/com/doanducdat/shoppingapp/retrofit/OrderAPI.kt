package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.order.Order
import com.doanducdat.shoppingapp.module.response.ResponseOrder
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface OrderAPI {

    @POST("api/users/orders")
    suspend fun order(
        @Header("Authorization") authorization: String,
        @Body order: Order
    ): ResponseOrder

}
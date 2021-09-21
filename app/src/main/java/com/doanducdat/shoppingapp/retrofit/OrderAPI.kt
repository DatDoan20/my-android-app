package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.order.Order
import com.doanducdat.shoppingapp.module.response.ResponseOrder
import retrofit2.http.*


interface OrderAPI {

    @POST("api/users/orders")
    suspend fun order(
        @Header("Authorization") authorization: String,
        @Body order:Order
    ): ResponseOrder

    @GET("api/users/orders/me")
    suspend fun getOrder(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): ResponseOrder

}
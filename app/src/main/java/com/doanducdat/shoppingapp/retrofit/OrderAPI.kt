package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.model.response.ResponseNotifyOrder
import com.doanducdat.shoppingapp.model.response.ResponseOrder
import com.doanducdat.shoppingapp.model.response.ResponseOrderPost
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.model.user.ReadAllOrderNoti
import retrofit2.http.*


interface OrderAPI {

    @POST("api/users/orders")
    suspend fun order(
        @Header("Authorization") authorization: String,
        @Body order: Order
    ): ResponseOrderPost

    @GET("api/users/orders/me")
    suspend fun getOrder(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): ResponseOrder

    @DELETE("api/users/orders/{id}/force")
    suspend fun cancelOrder(
        @Header("Authorization") authorization: String,
        @Path("id") idOrder: String
    ): ResponseOrder

    //get my received order to show product in those to user can review it
    @GET("api/users/orders/me/{state}")
    suspend fun getMyOrderByState(
        @Header("Authorization") authorization: String,
        @Path("state") state: String
    ): ResponseOrder

    @GET("api/users/notify-orders/me/limit/{limit}/page/{page}")
    suspend fun getNotifyOrder(
        @Header("Authorization") authorization: String,
        @Path("limit") limit: Int,
        @Path("page") page: Int
    ): ResponseNotifyOrder

    @PATCH("api/users/notify-orders/{idNotifyOrder}")
    suspend fun checkReadNotifyOrder(
        @Header("Authorization") authorization: String,
        @Path("idNotifyOrder") idNotifyOrder: String,
    ): ResponseNotifyOrder

    @DELETE(" api/users/notify-orders/{idNotifyOrder}/force")
    suspend fun deleteNotifyOrder(
        @Header("Authorization") authorization: String,
        @Path("idNotifyOrder") idNotifyOrder: String,
    ): ResponseNotifyOrder

    @DELETE(" api/users/notify-orders/me/all/force")
    suspend fun deleteAllNotifyOrder(
        @Header("Authorization") authorization: String,
    ): ResponseNotifyOrder

    @PATCH(" api/users/notify-orders/me/all")
    suspend fun checkReadAllNotifyOrder(
        @Header("Authorization") authorization: String,
        @Body readAllOrderNoti: ReadAllOrderNoti
    ): ResponseUser


}
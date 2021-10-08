package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.model.response.ResponseReview
import com.doanducdat.shoppingapp.model.review.ReviewPost
import retrofit2.http.*

interface ReviewAPI {

    @GET("api/users/reviews/search")
    suspend fun getReview(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("productId") productId: String
    ): ResponseReview

    //orderId use update stateRating of product
    @POST("api/users/reviews/{productId}/{orderId}")
    suspend fun createReview(
        @Header("Authorization") authorization: String,
        @Path("productId") productId: String,
        @Body reviewPost: ReviewPost,
        @Path("orderId") orderId: String
    ): ResponseReview
}
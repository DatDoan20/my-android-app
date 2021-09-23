package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.response.ResponseComment
import com.doanducdat.shoppingapp.module.response.ResponseProduct
import com.doanducdat.shoppingapp.module.response.ResponseReview
import com.doanducdat.shoppingapp.module.review.CommentPost
import retrofit2.http.*


interface ProductAPI {

    /**
     * All query default is descending Time -> query by newest
     * param discountDifferent: 0 -> get product is discount
     * category: nam/nu
     * type: vay-suong/...
     */
    @GET("api/products/search")
    suspend fun getProducts(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("discount[ne]") discountDifferent: Int? = null,
        @Query("category") category: String? = null,
        @Query("type") type: String? = null,
    ): ResponseProduct

    @GET("api/users/reviews/search")
    suspend fun getReview(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("productId") productId: String
    ): ResponseReview

    @GET("api/users/reviews/comments/search")
    suspend fun getComment(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("reviewId") reviewId: String
    ): ResponseComment

    @POST("api/users/reviews/{reviewId}/comment") //reviewId
    suspend fun createComment(
        @Header("Authorization") authorization: String,
        @Path("reviewId") reviewId: String,
        @Body comment: CommentPost
    ): ResponseComment
}
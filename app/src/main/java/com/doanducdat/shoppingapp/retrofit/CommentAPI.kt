package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.model.response.ResponseComment
import com.doanducdat.shoppingapp.model.response.ResponseNotifyComment
import com.doanducdat.shoppingapp.model.review.CommentPost
import retrofit2.http.*

interface CommentAPI {

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

    //get notify comment
    @GET("api/users/notify-comments/me/limit/{limit}/page/{page}")
    suspend fun getNotifyComment(
        @Header("Authorization") authorization: String,
        @Path("limit") limit: Int,
        @Path("page") page: Int
    ): ResponseNotifyComment

    //get notify comment
    @GET("api/users/notify-comments/{idNotifyComment}")
    suspend fun checkReadNotifyComment(
        @Header("Authorization") authorization: String,
        @Path("idNotifyComment") idNotifyComment: String
    ): ResponseNotifyComment
}
package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.model.response.ResponseComment
import com.doanducdat.shoppingapp.model.response.ResponseNotifyComment
import com.doanducdat.shoppingapp.model.review.CommentPost
import com.doanducdat.shoppingapp.model.user.ReadAllCommentNoti
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

    @GET("api/users/notify-comments/me/limit/{limit}/page/{page}")
    suspend fun getNotifyComment(
        @Header("Authorization") authorization: String,
        @Path("limit") limit: Int,
        @Path("page") page: Int
    ): ResponseNotifyComment

    @PATCH("api/users/notify-comments/{idNotifyComment}/read/me")
    suspend fun checkReadNotifyComment(
        @Header("Authorization") authorization: String,
        @Path("idNotifyComment") idNotifyComment: String
    ): ResponseNotifyComment

    @PATCH("api/users/notify-comments/me/read/all")
    suspend fun checkReadAllNotifyComment(
        @Header("Authorization") authorization: String,
        @Body readAllCommentNoti: ReadAllCommentNoti
    ): ResponseNotifyComment

    /**
     * remove me in list receiverIds in Notify Comment
     */
    @PATCH("api/users/notify-comments/{idNotifyComment}/remove/me")
    suspend fun deleteNotifyComment(
        @Header("Authorization") authorization: String,
        @Path("idNotifyComment") idNotifyComment: String
    ): ResponseNotifyComment

    /**
     * remove me in list receiverIds in All Notify Comment
     */
    @PATCH("api/users/notify-comments/remove/me/all")
    suspend fun deleteAllNotifyComment(
        @Header("Authorization") authorization: String,
    ): ResponseNotifyComment
}
package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.review.Comment
import com.doanducdat.shoppingapp.model.review.CommentPost
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.paging.CommentPagingSource
import com.doanducdat.shoppingapp.paging.NotifyCommentPagingSource
import com.doanducdat.shoppingapp.retrofit.CommentAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentAPI: CommentAPI
) : BaseRepository() {

    fun getCommentPaging(reviewId: String): Flow<PagingData<Comment>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        CommentPagingSource(commentAPI, reviewId)
    }.flow

    fun getNotifyCommentPaging(): Flow<PagingData<NotifyComment>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        NotifyCommentPagingSource(commentAPI)
    }.flow

    suspend fun createComment(reviewId: String, comment: CommentPost) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseComment = commentAPI.createComment(token, reviewId, comment)
            emit(DataState.success(responseComment))
        }, IO
    )

    suspend fun checkReadNotifyComment(idNotifyComment: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseNotifyComment = commentAPI.checkReadNotifyComment(token, idNotifyComment)
            emit(DataState.success(responseNotifyComment))
        }, IO
    )
}
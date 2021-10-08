package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.review.Review
import com.doanducdat.shoppingapp.model.review.ReviewPost
import com.doanducdat.shoppingapp.paging.ReviewPagingSource
import com.doanducdat.shoppingapp.retrofit.ReviewAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewAPI: ReviewAPI
) : BaseRepository() {


    fun getReviewPaging(productId: String): Flow<PagingData<Review>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        ReviewPagingSource(reviewAPI, productId)
    }.flow

    suspend fun createReview(productId: String, reviewPost: ReviewPost, orderId: String) =
        safeThreadDefaultCatch(
            flow {
                emit(loading)
                val responseReview = reviewAPI.createReview(token, productId, reviewPost, orderId)
                emit(DataState.success(responseReview))
            }, IO
        )
}
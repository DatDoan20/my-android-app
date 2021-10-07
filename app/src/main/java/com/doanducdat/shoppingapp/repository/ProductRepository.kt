package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.product.ProductId
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.review.*
import com.doanducdat.shoppingapp.paging.CommentPagingSource
import com.doanducdat.shoppingapp.paging.NotifyCommentPagingSource
import com.doanducdat.shoppingapp.paging.ProductPagingSource
import com.doanducdat.shoppingapp.paging.ReviewPagingSource
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI,
    private val userAPI: UserAPI
) : BaseRepository() {

    suspend fun getIntroSaleProducts() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resSaleProduct =
                productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1, str.DISCOUNTING)
            emit(DataState.success(resSaleProduct))
        }, IO
    )

    suspend fun getIntroNewProducts() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resNewProduct = productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1)
            emit(DataState.success(resNewProduct))
        }, IO
    )

    fun getProductPaging(category: String?, type: String?): Flow<PagingData<Product>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        ProductPagingSource(productAPI, category, type)
    }.flow

    fun getReviewPaging(productId: String): Flow<PagingData<Review>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        ReviewPagingSource(productAPI, productId)
    }.flow

    fun getCommentPaging(reviewId: String): Flow<PagingData<Comment>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        CommentPagingSource(productAPI, reviewId)
    }.flow

    fun getNotifyCommentPaging(): Flow<PagingData<NotifyComment>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        NotifyCommentPagingSource(productAPI)
    }.flow

    suspend fun addToCart(carts: Cart) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resHandleProductInCard = userAPI.addToCart(token, carts)
            emit(DataState.success(resHandleProductInCard))
        }, IO
    )

    suspend fun deleteProductInCart(idProduct: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resHandleProductInCard = userAPI.deleteProductInCart(token, ProductId(idProduct))
            emit(DataState.success(resHandleProductInCard))
        }, IO
    )

    suspend fun createComment(reviewId: String, comment: CommentPost) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseComment = productAPI.createComment(token, reviewId, comment)
            emit(DataState.success(responseComment))
        }, IO
    )

    suspend fun createReview(productId: String, reviewPost: ReviewPost, orderId: String) =
        safeThreadDefaultCatch(
            flow {
                emit(loading)
                val responseReview = productAPI.createReview(token, productId, reviewPost, orderId)
                emit(DataState.success(responseReview))
            }, IO
        )
}
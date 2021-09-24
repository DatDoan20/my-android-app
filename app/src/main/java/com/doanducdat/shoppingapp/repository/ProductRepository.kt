package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.module.cart.Cart
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.module.product.ProductId
import com.doanducdat.shoppingapp.module.response.*
import com.doanducdat.shoppingapp.module.review.Comment
import com.doanducdat.shoppingapp.module.review.CommentPost
import com.doanducdat.shoppingapp.module.review.Review
import com.doanducdat.shoppingapp.module.review.ReviewPost
import com.doanducdat.shoppingapp.paging.CommentPagingSource
import com.doanducdat.shoppingapp.paging.ProductPagingSource
import com.doanducdat.shoppingapp.paging.ReviewPagingSource
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI,
    private val userAPI: UserAPI
) {

    suspend fun getIntroSaleProducts() = flow {
        emit(DataState.loading(null))
        try {
            val resSaleProduct: ResponseProduct =
                productAPI.getProducts(
                    InfoUser.token.toString(),
                    AppConstants.QueryRequest.LIMIT_10,
                    AppConstants.QueryRequest.PAGE_1,
                    AppConstants.QueryRequest.DISCOUNTING,
                )
            emit(DataState.success(resSaleProduct))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

    suspend fun getIntroNewProducts() = flow {
        emit(DataState.loading(null))
        try {
            val resNewProduct: ResponseProduct =
                productAPI.getProducts(
                    InfoUser.token.toString(),
                    AppConstants.QueryRequest.LIMIT_10,
                    AppConstants.QueryRequest.PAGE_1,
                )
            emit(DataState.success(resNewProduct))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

    fun getProductPaging(category: String?, type: String?): Flow<PagingData<Product>> = Pager(
        PagingConfig(pageSize = AppConstants.QueryRequest.LIMIT_8, enablePlaceholders = false),
    ) {
        ProductPagingSource(productAPI, category, type)
    }.flow

    fun getReviewPaging(productId: String): Flow<PagingData<Review>> = Pager(
        PagingConfig(pageSize = AppConstants.QueryRequest.LIMIT_8, enablePlaceholders = false),
    ) {
        ReviewPagingSource(productAPI, productId)
    }.flow

    fun getCommentPaging(reviewId: String): Flow<PagingData<Comment>> = Pager(
        PagingConfig(pageSize = AppConstants.QueryRequest.LIMIT_8, enablePlaceholders = false),
    ) {
        CommentPagingSource(productAPI, reviewId)
    }.flow

    suspend fun addToCart(carts: Cart) = flow {
        emit(DataState.loading(null))
        try {
            val resHandleProductInCard: ResponseHandleProductInCart =
                userAPI.addToCart(
                    InfoUser.token.toString(),
                    carts
                )
            emit(DataState.success(resHandleProductInCard))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

    suspend fun deleteProductInCart(idProduct: String) = flow {
        emit(DataState.loading(null))
        try {
            val resHandleProductInCard: ResponseHandleProductInCart =
                userAPI.deleteProductInCart(
                    InfoUser.token.toString(),
                    ProductId(idProduct)
                )
            emit(DataState.success(resHandleProductInCard))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

    suspend fun createComment(reviewId: String, comment: CommentPost) = flow {
        emit(DataState.loading(null))
        try {
            val responseComment: ResponseComment =
                productAPI.createComment(
                    InfoUser.token.toString(),
                    reviewId,
                    comment,
                )
            emit(DataState.success(responseComment))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

    suspend fun createReview(productId: String, reviewPost: ReviewPost, orderId:String) = flow {
        emit(DataState.loading(null))
        try {
            val responseReview: ResponseReview =
                productAPI.createReview(
                    InfoUser.token.toString(),
                    productId,
                    reviewPost,
                    orderId
                )
            emit(DataState.success(responseReview))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
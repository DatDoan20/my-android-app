package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.module.response.ResponseProduct
import com.doanducdat.shoppingapp.paging.ProductPagingSource
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI
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

    fun getProductPaging(): Flow<PagingData<Product>> = Pager(
        PagingConfig(pageSize = 10, enablePlaceholders = false),
    ) {
        ProductPagingSource(productAPI)
    }.flow
}
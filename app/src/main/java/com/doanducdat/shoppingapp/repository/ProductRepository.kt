package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.module.response.ResponseProduct
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI
) {
    suspend fun getProducts(token:String) = flow {
        emit(DataState.loading(null))
        try {
            val responseProducts: ResponseProduct = productAPI.getProducts(token)
            emit(DataState.success(responseProducts))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
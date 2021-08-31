package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.module.ResponseAuth
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI
) {
    suspend fun getProducts() = flow {
        emit(DataState.loading(null))
        try {
            val responseProducts: ResponseAuth = productAPI.getProducts()
            emit(DataState.success(responseProducts))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.module.response.ResponseOrder
import com.doanducdat.shoppingapp.retrofit.OrderAPI
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderAPI: OrderAPI
) {

    suspend fun order(order: Order) = flow {
        emit(DataState.loading(null))
        try {
            val responseOrder: ResponseOrder =
                orderAPI.order(
                    InfoUser.token.toString(),
                    order
                )
            emit(DataState.success(responseOrder))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
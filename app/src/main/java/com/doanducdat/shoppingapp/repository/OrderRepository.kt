package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseOrder
import com.doanducdat.shoppingapp.paging.NotifyOrderPagingSource
import com.doanducdat.shoppingapp.paging.OrderPagingSource
import com.doanducdat.shoppingapp.retrofit.OrderAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.validation.ResCatch
import kotlinx.coroutines.flow.Flow
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
            emit(DataState.error(null, ResCatch.errMsg(e)))
        }
    }

    fun getOrderPaging(): Flow<PagingData<Order>> = Pager(
        PagingConfig(pageSize = AppConstants.QueryRequest.LIMIT_5, enablePlaceholders = false),
    ) {
        OrderPagingSource(orderAPI)
    }.flow

    fun getNotifyOrderPaging(): Flow<PagingData<NotifyOrder>> = Pager(
        PagingConfig(pageSize = AppConstants.QueryRequest.LIMIT_8, enablePlaceholders = false),
    ) {
        NotifyOrderPagingSource(orderAPI)
    }.flow

    suspend fun cancelOrder(idOrder: String) = flow {
        emit(DataState.loading(null))
        try {
            val responseOrder: ResponseOrder =
                orderAPI.cancelOrder(
                    InfoUser.token.toString(),
                    idOrder
                )
            emit(DataState.success(responseOrder))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResCatch.errMsg(e)))
        }
    }

    suspend fun getMyOrderByState(state: String) = flow {
        emit(DataState.loading(null))
        try {
            val responseOrder: ResponseOrder =
                orderAPI.getMyOrderByState(
                    InfoUser.token.toString(),
                    state
                )
            emit(DataState.success(responseOrder))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResCatch.errMsg(e)))
        }
    }
}
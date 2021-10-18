package com.doanducdat.shoppingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseOrder
import com.doanducdat.shoppingapp.model.response.ResponseOrderPost
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.model.user.ReadAllOrderNoti
import com.doanducdat.shoppingapp.paging.NotifyOrderPagingSource
import com.doanducdat.shoppingapp.paging.OrderPagingSource
import com.doanducdat.shoppingapp.retrofit.OrderAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderAPI: OrderAPI
) : BaseRepository() {

    suspend fun order(order: Order) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseOrder: ResponseOrderPost = orderAPI.order(token, order)
            emit(DataState.success(responseOrder))
        }, IO
    )

    fun getOrderPaging(): Flow<PagingData<Order>> = Pager(
        PagingConfig(pageSize = str.LIMIT_5, enablePlaceholders = false),
    ) {
        OrderPagingSource(orderAPI)
    }.flow

    fun getNotifyOrderPaging(): Flow<PagingData<NotifyOrder>> = Pager(
        PagingConfig(pageSize = str.LIMIT_5, enablePlaceholders = false),
    ) {
        NotifyOrderPagingSource(orderAPI)
    }.flow

    suspend fun cancelOrder(idOrder: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseOrder: ResponseOrder = orderAPI.cancelOrder(token, idOrder)
            emit(DataState.success(responseOrder))
        }, IO
    )

    suspend fun getMyOrderByState(state: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseOrder: ResponseOrder = orderAPI.getMyOrderByState(token, state)
            emit(DataState.success(responseOrder))
        }, IO
    )

    suspend fun checkReadNotifyOrder(idNotifyOrder: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseNotifyOrder = orderAPI.checkReadNotifyOrder(token, idNotifyOrder)
            emit(DataState.success(responseNotifyOrder))
        }, IO
    )

    suspend fun checkReadAllNotifyOrder(
        readAllOrderNoti: ReadAllOrderNoti
    ): Flow<DataState<ResponseUser>> = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseUser = orderAPI.checkReadAllNotifyOrder(token, readAllOrderNoti)
            emit(DataState.success(responseUser))
        }, IO
    )

    suspend fun deleteNotifyOrder(idNotifyOrder: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseNotifyOrder = orderAPI.deleteNotifyOrder(token, idNotifyOrder)
            emit(DataState.success(responseNotifyOrder))
        }, IO
    )

    suspend fun deleteAllNotifyOrder() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseNotifyOrder = orderAPI.deleteAllNotifyOrder(token)
            emit(DataState.success(responseNotifyOrder))
        }, IO
    )

}
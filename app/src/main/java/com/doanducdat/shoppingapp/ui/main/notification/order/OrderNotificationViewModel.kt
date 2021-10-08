package com.doanducdat.shoppingapp.ui.main.notification.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.doanducdat.shoppingapp.adapter.NotifyOrderPagingAdapter
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseNotifyOrder
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.model.user.ReadAllOrderNoti
import com.doanducdat.shoppingapp.repository.OrderRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrderNotificationViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
) : BaseViewModel() {
    var myPagingNotifyOrder: PagingData<NotifyOrder>? = null

    private val _dataStateReadOrder: MutableLiveData<DataState<ResponseNotifyOrder>> =
        MutableLiveData()
    val dataStateReadOrder: LiveData<DataState<ResponseNotifyOrder>>
        get() = _dataStateReadOrder

    private val _dataStateReadAllOrder: MutableLiveData<DataState<ResponseUser>> =
        MutableLiveData()
    val dataStateReadAllOrder: LiveData<DataState<ResponseUser>>
        get() = _dataStateReadAllOrder

    private val _dataStateDeleteOrder: MutableLiveData<DataState<ResponseNotifyOrder>> =
        MutableLiveData()
    val dataStateDeleteOrder: LiveData<DataState<ResponseNotifyOrder>>
        get() = _dataStateDeleteOrder

    private val _dataStateDeleteAllOrder: MutableLiveData<DataState<ResponseNotifyOrder>> =
        MutableLiveData()
    val dataStateDeleteAllOrder: LiveData<DataState<ResponseNotifyOrder>>
        get() = _dataStateDeleteAllOrder

    fun getNotifyOrderPaging(): Flow<PagingData<NotifyOrder>> {
        return orderRepository.getNotifyOrderPaging().cachedIn(viewModelScope)
    }

    fun checkReadNotifyOrder(idNotifyOrder: String) = viewModelScope.launch(NonCancellable) {
        orderRepository.checkReadNotifyOrder(idNotifyOrder).onEach {
            _dataStateReadOrder.value = it
        }.launchIn(viewModelScope)
    }

    fun checkReadAllNotifyOrder() = viewModelScope.launch(NonCancellable) {
        val readAllOrderNoti = ReadAllOrderNoti(Calendar.getInstance().time)
        orderRepository.checkReadAllNotifyOrder(readAllOrderNoti).onEach {
            _dataStateReadAllOrder.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteNotifyOrder(idNotifyOrder: String) = viewModelScope.launch(NonCancellable) {
        orderRepository.deleteNotifyOrder(idNotifyOrder).onEach {
            _dataStateDeleteOrder.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteClientSideNotifyOrder(
        idDeletedNotifyOrder: String,
        notifyOrderPagingAdapter: NotifyOrderPagingAdapter
    ) = viewModelScope.launch {
        withContext(scopeIO) { //it still first scope
            myPagingNotifyOrder = myPagingNotifyOrder?.filter {
                it.id != idDeletedNotifyOrder
            }
        }
        myPagingNotifyOrder?.let { notifyOrderPagingAdapter.submitData(it) }
    }

    fun deleteAllNotifyOrder() = viewModelScope.launch(NonCancellable) {
        orderRepository.deleteAllNotifyOrder().onEach {
            _dataStateDeleteAllOrder.value = it
        }.launchIn(viewModelScope)
    }
}
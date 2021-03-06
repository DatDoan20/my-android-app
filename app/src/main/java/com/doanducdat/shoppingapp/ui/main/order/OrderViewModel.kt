package com.doanducdat.shoppingapp.ui.main.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.model.order.PurchasedProduct
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseOrder
import com.doanducdat.shoppingapp.model.response.ResponseOrderPost
import com.doanducdat.shoppingapp.repository.OrderRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    var state = AppConstants.QueryRequest.WAITING
    var paymentMode = AppConstants.QueryRequest.PAYMENT_MODE
    var name = ""
    var note = ""
    var email = ""
    var phone = ""
    var address = ""
    var purchasedProducts: MutableList<PurchasedProduct> = mutableListOf()

    var costDelivery: Int = 0
    var totalPrice: Int = 0
    var totalPayment: Int = 0

    private val _dataStateOrder: MutableLiveData<DataState<ResponseOrderPost>> = MutableLiveData()
    val dataStateOrder: LiveData<DataState<ResponseOrderPost>>
        get() = _dataStateOrder

    private val _dataStateCancel: MutableLiveData<DataState<ResponseOrder>> = MutableLiveData()
    val dataStateCancel: LiveData<DataState<ResponseOrder>>
        get() = _dataStateCancel

    private val _dataStateReceivedOrder: MutableLiveData<DataState<ResponseOrder>> =
        MutableLiveData()
    val dataStateReceivedOrder: LiveData<DataState<ResponseOrder>>
        get() = _dataStateReceivedOrder

    fun createOrder(paymentMode: String): Order {
        return Order(
            null,
            address,
            costDelivery,
            email,
            name,
            paymentMode,
            phone,
            state,
            totalPayment,
            totalPrice,
            purchasedProducts,
            null,
            note
        )
    }

    fun order(paymentMode: String = AppConstants.QueryRequest.PAYMENT_MODE) =
        viewModelScope.launch {
            orderRepository.order(createOrder(paymentMode)).onEach {
                _dataStateOrder.value = it
            }.launchIn(viewModelScope)
        }

    fun getOrderPaging(): Flow<PagingData<Order>> {
        return orderRepository.getOrderPaging().cachedIn(viewModelScope)
    }

    fun cancelOrder(idOrder: String) = viewModelScope.launch {
        orderRepository.cancelOrder(idOrder).onEach {
            _dataStateCancel.value = it
        }.launchIn(viewModelScope)
    }

    fun getMyReceivedOrder() = viewModelScope.launch {
        orderRepository.getMyOrderByState(AppConstants.Order.RECEIVED).onEach {
            _dataStateReceivedOrder.value = it
        }.launchIn(viewModelScope)
    }
}
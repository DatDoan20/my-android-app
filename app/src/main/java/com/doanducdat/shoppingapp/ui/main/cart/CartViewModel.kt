package com.doanducdat.shoppingapp.ui.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.module.order.Order
import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.module.response.ResponseHandleProductInCart
import com.doanducdat.shoppingapp.module.response.ResponseOrder
import com.doanducdat.shoppingapp.repository.OrderRepository
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var sumMoneyCart: MutableLiveData<Int> = MutableLiveData(0)

    private val _dataStateDeleteProductInCart: MutableLiveData<DataState<ResponseHandleProductInCart>> =
        MutableLiveData()
    val dataStateDeleteProductInCart: LiveData<DataState<ResponseHandleProductInCart>>
        get() = _dataStateDeleteProductInCart

    private val _dataStateOrder: MutableLiveData<DataState<ResponseOrder>> =
        MutableLiveData()
    val dataStateOrder: LiveData<DataState<ResponseOrder>>
        get() = _dataStateOrder

    fun deleteProductInCart(idProduct: String) = viewModelScope.launch {
        productRepository.deleteProductInCart(idProduct).onEach {
            _dataStateDeleteProductInCart.value = it
        }.launchIn(viewModelScope)
    }

    fun order(order: Order) = viewModelScope.launch {
        orderRepository.order(order).onEach {
            _dataStateOrder.value = it
        }.launchIn(viewModelScope)
    }
}
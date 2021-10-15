package com.doanducdat.shoppingapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseProduct
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel() {
    private val _dataStateSaleProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val dataStateSaleProducts: LiveData<DataState<ResponseProduct>>
        get() = _dataStateSaleProducts

    private val _dataStateNewProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val dataStateNewProducts: LiveData<DataState<ResponseProduct>>
        get() = _dataStateNewProducts

    private val _freshNewProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val freshNewProducts: LiveData<DataState<ResponseProduct>>
        get() = _freshNewProducts

    private val _freshSaleProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val freshSaleProducts: LiveData<DataState<ResponseProduct>>
        get() = _freshSaleProducts

    fun getSaleProduct() = viewModelScope.launch {
        productRepository.getSaleProduct().onEach {
            _dataStateSaleProducts.value = it
        }.launchIn(viewModelScope)
    }

    fun getNewProducts() = viewModelScope.launch {
        productRepository.getNewProduct().onEach {
            _dataStateNewProducts.value = it
        }.launchIn(viewModelScope)
    }

    fun freshSaleProduct() = viewModelScope.launch {
        productRepository.getSaleProductAPI().onEach {
            _freshNewProducts.value = it
        }.launchIn(viewModelScope)
    }

    fun freshNewProduct() = viewModelScope.launch {
        productRepository.getNewProductAPI().onEach {
            _freshNewProducts.value = it
        }.launchIn(viewModelScope)
    }

}
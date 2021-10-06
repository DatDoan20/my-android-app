package com.doanducdat.shoppingapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseProduct
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val _dataStateSaleProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val dataStateSaleProducts: LiveData<DataState<ResponseProduct>>
        get() = _dataStateSaleProducts

    private val _dataStateNewProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val dataStateNewProducts: LiveData<DataState<ResponseProduct>>
        get() = _dataStateNewProducts


    fun getIntroSaleProducts() = viewModelScope.launch {
        productRepository.getIntroSaleProducts().onEach {
            _dataStateSaleProducts.value = it
        }.launchIn(viewModelScope)
    }

    fun getIntroNewProducts() = viewModelScope.launch {
        productRepository.getIntroNewProducts().onEach {
            _dataStateNewProducts.value = it
        }.launchIn(viewModelScope)
    }


}
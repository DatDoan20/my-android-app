package com.doanducdat.shoppingapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.module.ResponseAuth
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.utils.response.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<ResponseAuth>> = MutableLiveData()
    val dataState: LiveData<DataState<ResponseAuth>>
        get() = _dataState

    fun getProducts() = viewModelScope.launch {
        productRepository.getProducts().onEach {
            _dataState.value = it
        }.launchIn(viewModelScope)
    }
}
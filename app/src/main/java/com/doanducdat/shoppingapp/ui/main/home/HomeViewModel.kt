package com.doanducdat.shoppingapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.module.response.ResponseProduct
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.response.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<ResponseProduct>> = MutableLiveData()
    val dataState: LiveData<DataState<ResponseProduct>>
        get() = _dataState

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getProducts() = viewModelScope.launch {
        productRepository.getProducts(InfoUser.token.toString()).onEach {
            _dataState.value = it
        }.launchIn(viewModelScope)
    }
}
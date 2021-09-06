package com.doanducdat.shoppingapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.module.response.ResponseProduct
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.response.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {


    private val _dataStateSaleProducts: MutableLiveData<DataState<ResponseProduct>> =
        MutableLiveData()
    val dataStateSaleProducts: LiveData<DataState<ResponseProduct>>
        get() = _dataStateSaleProducts


    fun getSaleProducts() = viewModelScope.launch {
        productRepository.getSaleProducts(InfoUser.token.toString()).onEach {
            _dataStateSaleProducts.value = it
        }.launchIn(viewModelScope)
    }

    fun getNewProducts(): Flow<PagingData<Product>> {
        return productRepository.getNewProducts().cachedIn(viewModelScope)
    }
}
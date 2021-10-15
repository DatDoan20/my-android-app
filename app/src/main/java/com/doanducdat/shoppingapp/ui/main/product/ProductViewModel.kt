package com.doanducdat.shoppingapp.ui.main.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseHandleProductInCart
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _dataStateHandleProductInCart: MutableLiveData<DataState<ResponseHandleProductInCart>> =
        MutableLiveData()
    val dataStateHandleProductInCart: LiveData<DataState<ResponseHandleProductInCart>>
        get() = _dataStateHandleProductInCart


    fun getProductPaging(category: String?, type: String?): Flow<PagingData<Product>> {
        return productRepository.getProductPagingAPI(category, type).cachedIn(viewModelScope)
    }

    fun addToCart(carts: Cart)= viewModelScope.launch {
        productRepository.addToCart(carts).onEach {
            _dataStateHandleProductInCart.value = it
        }.launchIn(viewModelScope)
    }
}
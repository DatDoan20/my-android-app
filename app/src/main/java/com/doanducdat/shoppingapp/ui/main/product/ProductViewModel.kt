package com.doanducdat.shoppingapp.ui.main.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    fun getProductPaging(category: String?, type: String?): Flow<PagingData<Product>> {
        return productRepository.getProductPaging(category, type).cachedIn(viewModelScope)
    }
}
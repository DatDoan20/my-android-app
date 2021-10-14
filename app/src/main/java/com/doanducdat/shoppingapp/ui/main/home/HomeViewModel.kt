package com.doanducdat.shoppingapp.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseProduct
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.room.entity.ImageCacheEntity
import com.doanducdat.shoppingapp.room.entity.toListProduct
import com.doanducdat.shoppingapp.room.entity.toListProductCacheEntity
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun getIntroSaleProducts() = viewModelScope.launch {
        withContext(scopeIO) {
            val productsCache = productRepository.getAllProduct()
            if (productsCache.isEmpty()) {
                withContext(scopeMAIN) {
                    Log.e("TEST_ROOM", "get products api: ${Thread.currentThread().name}")
                    productRepository.getIntroSaleProducts().onEach {
                        _dataStateSaleProducts.value = it
                    }.launchIn(viewModelScope)
                }
            } else {
                Log.e("TEST_ROOM", "get images all product cache: ${Thread.currentThread().name}")
                val imagesCacheOfProduct = productRepository.getImagesOfProduct()
                withContext(scopeMAIN) {
                    val res = ResponseProduct(
                        "200",
                        productsCache.toListProduct(imagesCacheOfProduct),
                        null,
                        null
                    )
                    _dataStateSaleProducts.value = DataState.success(res)
                }
            }

        }
    }

    fun getIntroNewProducts() = viewModelScope.launch {
        productRepository.getIntroNewProducts().onEach {
            _dataStateNewProducts.value = it
        }.launchIn(viewModelScope)
    }

    /** ROOM */
    // (1) insert product, (2) insert images of each product
    fun insertAllProduct(products: List<Product>) = viewModelScope.launch() {
        launch(Dispatchers.IO) {
            productRepository.insertAllProduct(products.toListProductCacheEntity())
        }
        launch(Dispatchers.IO) {
            Log.e("TEST_ROOM", "get images of all product : ${Thread.currentThread().name}")

            val imagesOfAllProduct: MutableList<ImageCacheEntity> = mutableListOf()
            products.forEach { itemProduct ->
                itemProduct.getImages().forEach { name ->
                    imagesOfAllProduct.add(ImageCacheEntity(itemProduct.id, name))
                }
            }
            productRepository.insertImagesOfProduct(imagesOfAllProduct)
        }
    }


}
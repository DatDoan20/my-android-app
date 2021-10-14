package com.doanducdat.shoppingapp.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.product.ProductId
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.paging.ProductPagingSource
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.room.dao.ImageDao
import com.doanducdat.shoppingapp.room.dao.ProductDao
import com.doanducdat.shoppingapp.room.entity.ImageCacheEntity
import com.doanducdat.shoppingapp.room.entity.ProductCacheEntity
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI,
    private val productDao: ProductDao,
    private val imageDao: ImageDao,
    private val userAPI: UserAPI
) : BaseRepository() {

    suspend fun getIntroSaleProducts() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resSaleProduct =
                productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1, str.DISCOUNTING)
            emit(DataState.success(resSaleProduct))
        }, IO
    )

    suspend fun getIntroNewProducts() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resNewProduct = productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1)
            emit(DataState.success(resNewProduct))
        }, IO
    )

    fun getProductPaging(category: String?, type: String?): Flow<PagingData<Product>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        ProductPagingSource(productAPI, category, type)
    }.flow


    suspend fun addToCart(carts: Cart) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resHandleProductInCard = userAPI.addToCart(token, carts)
            emit(DataState.success(resHandleProductInCard))
        }, IO
    )

    suspend fun deleteProductInCart(idProduct: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resHandleProductInCard = userAPI.deleteProductInCart(token, ProductId(idProduct))
            emit(DataState.success(resHandleProductInCard))
        }, IO
    )

    /** ROOM */
    suspend fun insertAllProduct(products: List<ProductCacheEntity>) {
        Log.e("TEST_ROOM", "insert all product : ${Thread.currentThread().name}")
        productDao.insertAll(products)
    }

    suspend fun insertImagesOfProduct(imagesOfAllProduct: MutableList<ImageCacheEntity>) {
        Log.e("TEST_ROOM", "insert images of all product : ${Thread.currentThread().name}")
        imageDao.insertAll(imagesOfAllProduct)
    }

    suspend fun getAllProduct(): List<ProductCacheEntity> {
        Log.e("TEST_ROOM", "get all product cache: ${Thread.currentThread().name}")
        return productDao.getAll()
    }

    suspend fun getImagesOfProduct() = imageDao.getAll()

}
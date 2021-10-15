package com.doanducdat.shoppingapp.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.product.ProductId
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseProduct
import com.doanducdat.shoppingapp.paging.ProductPagingSource
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.room.dao.ImageDao
import com.doanducdat.shoppingapp.room.dao.ProductDao
import com.doanducdat.shoppingapp.room.entity.ImageCacheEntity
import com.doanducdat.shoppingapp.room.entity.toListProduct
import com.doanducdat.shoppingapp.room.entity.toListProductCacheEntity
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI,
    private val productDao: ProductDao,
    private val imageDao: ImageDao,
    private val userAPI: UserAPI
) : BaseRepository() {

    suspend fun getSaleProduct() = safeThreadDefaultCatch(
        flow {
            emit(loading)
//            Log.e("TEST_ROOM", "get products cache: ${Thread.currentThread().name}")

            val saleProductCache = productDao.getSaleProduct()
            if (saleProductCache.isEmpty()) {
//                Log.e("TEST_ROOM", "get products api: ${Thread.currentThread().name}")
                val resSaleProductAPI =
                    productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1, str.DISCOUNTING)
                emit(DataState.success(resSaleProductAPI))

                // insert got product from API to ROOM
                insertAllProduct(resSaleProductAPI.data)
            } else {
//                Log.e("TEST_ROOM", "get image products cache: ${Thread.currentThread().name}")
                val saleProductsId = saleProductCache.map { it.id }
                val imagesCacheOfProduct = imageDao.getAll(saleProductsId)
                val saleProduct = saleProductCache.toListProduct(imagesCacheOfProduct)
                val resSaleProductCache = ResponseProduct("200", saleProduct, null, null)
                emit(DataState.success(resSaleProductCache))
            }
        }, IO
    )

    private fun insertAllProduct(products: List<Product>) =
        CoroutineScope(IO).launch(NonCancellable) {
            // (1) insert product,
            launch(IO) {
                Log.e("TEST_ROOM", "insert pro: ${Thread.currentThread().name}")
                productDao.insertAll(products.toListProductCacheEntity())
            }
            // (2) insert images of each product
            launch(IO) {
                val imagesOfAllProduct: MutableList<ImageCacheEntity> = mutableListOf()
                products.forEach { itemProduct ->
                    itemProduct.getImages().forEach { name ->
                        imagesOfAllProduct.add(ImageCacheEntity(itemProduct.id, name))
                    }
                }
                Log.e("TEST_ROOM", "insert image: ${Thread.currentThread().name}")
                imageDao.insertAll(imagesOfAllProduct)
            }
        }

    suspend fun getNewProduct() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val newProductCache = productDao.getNewProduct()
            if (newProductCache.isEmpty()) {
                val resNewProductAPI = productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1)
                emit(DataState.success(resNewProductAPI))
                insertAllProduct(resNewProductAPI.data)
            } else {
                val newProductsId = newProductCache.map { it.id }
                val imagesCacheOfProduct = imageDao.getAll(newProductsId)
                val newProduct = newProductCache.toListProduct(imagesCacheOfProduct)
                val resNewProductCache = ResponseProduct("200", newProduct, null, null)
                emit(DataState.success(resNewProductCache))
            }
        }, IO
    )

    fun getProductPagingAPI(category: String?, type: String?): Flow<PagingData<Product>> = Pager(
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

}
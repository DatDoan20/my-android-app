package com.doanducdat.shoppingapp.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.product.CardId
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseProduct
import com.doanducdat.shoppingapp.paging.ProductPagingSource
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.room.dao.ImageDao
import com.doanducdat.shoppingapp.room.dao.KeyWordDao
import com.doanducdat.shoppingapp.room.dao.ProductDao
import com.doanducdat.shoppingapp.room.entity.*
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productAPI: ProductAPI,
    private val productDao: ProductDao,
    private val imageDao: ImageDao,
    private val userAPI: UserAPI,
    private val keyWordDao: KeyWordDao
) : BaseRepository() {

    suspend fun getSaleProduct() = safeThreadDefaultCatch(
        flow {
            emit(loading)
//            Log.e("TEST_ROOM", "get products cache: ${Thread.currentThread().name}")
            val saleProductCacheWithoutImages = productDao.getSaleProduct()
            if (saleProductCacheWithoutImages.isEmpty()) {
//                Log.e("TEST_ROOM", "get products api: ${Thread.currentThread().name}")
                getSaleProductAPI()
                    .collect { dataStateSaleProduct -> emit(dataStateSaleProduct) }
            } else {
//                Log.e("TEST_ROOM", "get image products cache: ${Thread.currentThread().name}")
                getProductCacheAndImages(saleProductCacheWithoutImages)
                    .collect { dataStateSaleProduct -> emit(dataStateSaleProduct) }
            }
        }, IO
    )

    suspend fun getSaleProductAPI() = flow {
        val resSaleProductAPI =
            productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1, str.DISCOUNTING)
        emit(DataState.success(resSaleProductAPI))
        // insert product from API to ROOM
        insertProducts(resSaleProductAPI.data)
    }

    suspend fun getNewProductAPI() = flow {
        val resNewProductAPI = productAPI.getProducts(token, str.LIMIT_10, str.PAGE_1)
        emit(DataState.success(resNewProductAPI))
        // insert product from API to ROOM
        insertProducts(resNewProductAPI.data)
    }

    private suspend fun getProductCacheAndImages(productsCacheWithoutImages: List<ProductCacheEntity>) =
        flow {
            val productsId = productsCacheWithoutImages.map { it.id }
            val imagesCacheOfProduct = imageDao.getAll(productsId)
            val finishedProducts = productsCacheWithoutImages.toListProduct(imagesCacheOfProduct)
            val resFinishedProductsCache =
                ResponseProduct("200", finishedProducts, null, null)
            emit(DataState.success(resFinishedProductsCache))
        }

    private fun insertProducts(products: List<Product>) =
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
            val newProductCacheWithoutImages = productDao.getNewProduct()
            if (newProductCacheWithoutImages.isEmpty()) {
                getNewProductAPI()
                    .collect { dataStateNewProduct -> emit(dataStateNewProduct) }
            } else {
                getProductCacheAndImages(newProductCacheWithoutImages)
                    .collect { dataStateSaleProduct -> emit(dataStateSaleProduct) }
            }
        }, IO
    )

    fun getProductPagingAPI(
        category: String?,
        type: String?,
        discountDifferent: Int?,
        name: String?
    ): Flow<PagingData<Product>> = Pager(
        PagingConfig(pageSize = str.LIMIT_8, enablePlaceholders = false),
    ) {
        ProductPagingSource(productAPI, category, type, discountDifferent, name)
    }.flow

    suspend fun addToCart(carts: Cart) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resHandleProductInCard = userAPI.addToCart(token, carts)
            emit(DataState.success(resHandleProductInCard))
        }, IO
    )

    suspend fun deleteProductInCart(id: String) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val resHandleProductInCard = userAPI.deleteProductInCart(token, CardId(id))
            emit(DataState.success(resHandleProductInCard))
        }, IO
    )

    fun getAllKeyWord() = keyWordDao.getAll()

    suspend fun insertKeyWord(keyWord: KeyWordCacheEntity) = keyWordDao.insert(keyWord)
    suspend fun deleteAllKeyWord() = keyWordDao.deleteAll()
}
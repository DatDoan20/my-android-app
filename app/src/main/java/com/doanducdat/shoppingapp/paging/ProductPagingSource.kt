package com.doanducdat.shoppingapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.module.response.ResponseProduct
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import retrofit2.HttpException
import java.io.IOException


class ProductPagingSource(private val productAPI: ProductAPI) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: AppConstants.QueryRequest.PAGE_1
        //params.loadSize is 10 (LIMIT_10)
        return try {
            val products: ResponseProduct =
                productAPI.getProducts(InfoUser.token.toString(), params.loadSize, page)
            LoadResult.Page(
                data = products.data,
                prevKey = if (page == AppConstants.QueryRequest.PAGE_1) null else page - 1,
                nextKey = if (products.data.isEmpty()) null else page + (params.loadSize / AppConstants.QueryRequest.LIMIT_10)
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        }
    }

}
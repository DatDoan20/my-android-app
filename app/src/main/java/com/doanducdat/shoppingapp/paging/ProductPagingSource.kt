package com.doanducdat.shoppingapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.response.ResponseProduct
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import retrofit2.HttpException
import java.io.IOException


class ProductPagingSource(
    private val productAPI: ProductAPI,
    private val category: String?,
    private val type: String?
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: AppConstants.QueryRequest.PAGE_1
        return try {
            val response: ResponseProduct =
                productAPI.getProducts(
                    InfoLocalUser.localToken.toString(),
                    params.loadSize,
                    page,
                    null,
                    category,
                    type
                )
            val products = response.data
            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }

}
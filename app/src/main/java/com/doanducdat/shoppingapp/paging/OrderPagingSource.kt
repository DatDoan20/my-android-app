package com.doanducdat.shoppingapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.retrofit.OrderAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import retrofit2.HttpException
import java.io.IOException


class OrderPagingSource(
    private val orderAPI: OrderAPI,
) : PagingSource<Int, Order>() {

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return null
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: AppConstants.QueryRequest.PAGE_1
        return try {
            val response = orderAPI.getOrder(InfoUser.localToken.toString(), params.loadSize, page)
            val orders = response.data
            LoadResult.Page(
                data = orders,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (orders.isEmpty()) null else page + 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            val err = ex.code().toString() + "\n" + ex.response()?.errorBody()?.source()
            Log.e(AppConstants.TAG.ORDER_MANAGEMENT, err)
            LoadResult.Error(ex)
        }
    }

}
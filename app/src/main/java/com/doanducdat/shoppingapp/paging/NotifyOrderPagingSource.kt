package com.doanducdat.shoppingapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doanducdat.shoppingapp.module.order.NotifyOrder
import com.doanducdat.shoppingapp.retrofit.OrderAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import retrofit2.HttpException
import java.io.IOException


class NotifyOrderPagingSource(
    private val orderAPI: OrderAPI,
) : PagingSource<Int, NotifyOrder>() {


    override fun getRefreshKey(state: PagingState<Int, NotifyOrder>): Int? {
        return null
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotifyOrder> {
        val page = params.key ?: AppConstants.QueryRequest.PAGE_1
        return try {
            val response = orderAPI.getNotifyOrder(InfoUser.token.toString(), params.loadSize, page)
            val notifyOrders = response.data
            LoadResult.Page(
                data = notifyOrders,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (notifyOrders.isEmpty()) null else page + 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }

}
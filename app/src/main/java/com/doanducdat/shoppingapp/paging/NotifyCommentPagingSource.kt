package com.doanducdat.shoppingapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import retrofit2.HttpException
import java.io.IOException


class NotifyCommentPagingSource(
    private val productAPI: ProductAPI,
) : PagingSource<Int, NotifyComment>() {

    override fun getRefreshKey(state: PagingState<Int, NotifyComment>): Int? {
        return null
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotifyComment> {
        val page = params.key ?: AppConstants.QueryRequest.PAGE_1
        return try {
            val response =
                productAPI.getNotifyComment(InfoUser.localToken.toString(), params.loadSize, page)
            val notifyComments = response.data
            LoadResult.Page(
                data = notifyComments,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (notifyComments.isEmpty()) null else page + 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }

}
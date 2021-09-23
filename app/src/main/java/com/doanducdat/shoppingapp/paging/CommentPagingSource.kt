package com.doanducdat.shoppingapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doanducdat.shoppingapp.module.review.Comment
import com.doanducdat.shoppingapp.retrofit.ProductAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import retrofit2.HttpException
import java.io.IOException


class CommentPagingSource(
    private val productAPI: ProductAPI,
    private val reviewId: String,
) : PagingSource<Int, Comment>() {

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return null
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val page = params.key ?: AppConstants.QueryRequest.PAGE_1
        return try {
            val responseComment =
                productAPI.getComment(InfoUser.token.toString(), params.loadSize, page, reviewId)
            val comments = responseComment.data
            LoadResult.Page(
                data = comments,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (comments.isEmpty()) null else page + 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }

}
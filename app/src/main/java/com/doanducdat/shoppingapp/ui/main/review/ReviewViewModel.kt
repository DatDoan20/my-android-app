package com.doanducdat.shoppingapp.ui.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.module.response.ResponseComment
import com.doanducdat.shoppingapp.module.review.Comment
import com.doanducdat.shoppingapp.module.review.CommentPost
import com.doanducdat.shoppingapp.module.review.Review
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _dataStateCreateComment: MutableLiveData<DataState<ResponseComment>> =
        MutableLiveData()
    val dataStateCreateComment: LiveData<DataState<ResponseComment>>
        get() = _dataStateCreateComment

    fun getReviewPaging(productId: String): Flow<PagingData<Review>> {
        return productRepository.getReviewPaging(productId).cachedIn(viewModelScope)
    }

    fun getCommentPaging(reviewId: String): Flow<PagingData<Comment>> {
        return productRepository.getCommentPaging(reviewId).cachedIn(viewModelScope)
    }

    fun createComment(reviewId: String, comment: CommentPost) = viewModelScope.launch {
        productRepository.createComment(reviewId, comment).onEach {
            _dataStateCreateComment.value = it
        }.launchIn(viewModelScope)
    }
}
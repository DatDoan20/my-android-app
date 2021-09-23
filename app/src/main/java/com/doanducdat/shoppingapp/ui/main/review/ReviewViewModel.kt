package com.doanducdat.shoppingapp.ui.main.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.module.review.Review
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getReviewPaging(productId: String): Flow<PagingData<Review>> {
        return productRepository.getReviewPaging(productId).cachedIn(viewModelScope)
    }
}
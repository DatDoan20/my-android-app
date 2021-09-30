package com.doanducdat.shoppingapp.ui.main.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.module.review.NotifyComment
import com.doanducdat.shoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    fun getNotifyCommentPaging(): Flow<PagingData<NotifyComment>> {
        return productRepository.getNotifyCommentPaging().cachedIn(viewModelScope)
    }
}
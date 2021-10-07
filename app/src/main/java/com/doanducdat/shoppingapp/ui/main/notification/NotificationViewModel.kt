package com.doanducdat.shoppingapp.ui.main.notification

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.repository.OrderRepository
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    fun getNotifyCommentPaging(): Flow<PagingData<NotifyComment>> {
        return productRepository.getNotifyCommentPaging().cachedIn(viewModelScope)
    }

    fun getNotifyOrderPaging(): Flow<PagingData<NotifyOrder>> {
        return orderRepository.getNotifyOrderPaging().cachedIn(viewModelScope)
    }
}
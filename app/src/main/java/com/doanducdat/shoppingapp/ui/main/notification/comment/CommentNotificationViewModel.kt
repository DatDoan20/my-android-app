package com.doanducdat.shoppingapp.ui.main.notification.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseNotifyComment
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.repository.CommentRepository
import com.doanducdat.shoppingapp.repository.OrderRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentNotificationViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val commentRepository: CommentRepository
) : BaseViewModel() {
    var myPagingNotifyOrder: PagingData<NotifyOrder>? = null

    private val _dataStateReadComment: MutableLiveData<DataState<ResponseNotifyComment>> =
        MutableLiveData()
    val dataStateReadComment: LiveData<DataState<ResponseNotifyComment>>
        get() = _dataStateReadComment

    private val _dataStateDeleteComment: MutableLiveData<DataState<ResponseNotifyComment>> =
        MutableLiveData()
    val dataStateDeleteComment: LiveData<DataState<ResponseNotifyComment>>
        get() = _dataStateDeleteComment

    fun getNotifyCommentPaging(): Flow<PagingData<NotifyComment>> {
        return commentRepository.getNotifyCommentPaging().cachedIn(viewModelScope)
    }

    fun checkReadNotifyComment(idNotifyComment: String) = viewModelScope.launch(NonCancellable) {
        commentRepository.checkReadNotifyComment(idNotifyComment).onEach {
            _dataStateReadComment.value = it
        }.launchIn(viewModelScope)
    }
}
package com.doanducdat.shoppingapp.ui.main.notification.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.doanducdat.shoppingapp.adapter.NotifyCommentPagingAdapter
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseNotifyComment
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.model.user.ReadAllCommentNoti
import com.doanducdat.shoppingapp.repository.CommentRepository
import com.doanducdat.shoppingapp.repository.OrderRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommentNotificationViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val commentRepository: CommentRepository
) : BaseViewModel() {
    var myPagingNotifyComment: PagingData<NotifyComment>? = null

    private val _dataStateReadComment: MutableLiveData<DataState<ResponseNotifyComment>> =
        MutableLiveData()
    val dataStateReadComment: LiveData<DataState<ResponseNotifyComment>>
        get() = _dataStateReadComment

    private val _dataStateReadAllComment: MutableLiveData<DataState<ResponseNotifyComment>> =
        MutableLiveData()
    val dataStateReadAllComment: LiveData<DataState<ResponseNotifyComment>>
        get() = _dataStateReadAllComment


    private val _dataStateDeleteComment: MutableLiveData<DataState<ResponseNotifyComment>> =
        MutableLiveData()
    val dataStateDeleteComment: LiveData<DataState<ResponseNotifyComment>>
        get() = _dataStateDeleteComment

    private val _dataStateDeleteAllComment: MutableLiveData<DataState<ResponseNotifyComment>> =
        MutableLiveData()
    val dataStateDeleteAllComment: LiveData<DataState<ResponseNotifyComment>>
        get() = _dataStateDeleteAllComment

    fun getNotifyCommentPaging(): Flow<PagingData<NotifyComment>> {
        return commentRepository.getNotifyCommentPaging().cachedIn(viewModelScope)
    }

    fun checkReadNotifyComment(idNotifyComment: String) = viewModelScope.launch(NonCancellable) {
        commentRepository.checkReadNotifyComment(idNotifyComment).onEach {
            _dataStateReadComment.value = it
        }.launchIn(viewModelScope)
    }

    fun checkReadAllNotifyComment() = viewModelScope.launch(NonCancellable) {
        val readAllCommentNoti = ReadAllCommentNoti(Calendar.getInstance().time)
        commentRepository.checkReadAllNotifyComment(readAllCommentNoti).onEach {
            _dataStateReadAllComment.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteNotifyComment(idNotifyComment: String) = viewModelScope.launch(NonCancellable) {
        commentRepository.deleteNotifyComment(idNotifyComment).onEach {
            _dataStateDeleteComment.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteClientSideNotifyComment(
        idDeletedNotifyComment: String,
        notifyCommentAdapter: NotifyCommentPagingAdapter,
    ) = viewModelScope.launch {
        withContext(scopeIO) { //it still first scope
            myPagingNotifyComment = myPagingNotifyComment?.filter {
                it.id != idDeletedNotifyComment
            }
        }
        myPagingNotifyComment?.let { notifyCommentAdapter.submitData(it) }
    }

    fun deleteAllNotifyComment() = viewModelScope.launch(NonCancellable) {
        commentRepository.deleteAllNotifyComment().onEach {
            _dataStateDeleteAllComment.value = it
        }.launchIn(viewModelScope)
    }


}
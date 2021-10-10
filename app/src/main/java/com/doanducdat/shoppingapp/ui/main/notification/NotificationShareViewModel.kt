package com.doanducdat.shoppingapp.ui.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doanducdat.shoppingapp.adapter.NotifyCommentPagingAdapter
import com.doanducdat.shoppingapp.adapter.NotifyOrderPagingAdapter
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import com.doanducdat.shoppingapp.utils.InfoUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationShareViewModel @Inject constructor(
) : BaseViewModel() {
    private var _numberUnReadNotifyOrder: MutableLiveData<Int> = MutableLiveData(0)
    val numberUnReadNotifyOrder: LiveData<Int>
        get() = _numberUnReadNotifyOrder

    private var _numberUnReadNotifyComment: MutableLiveData<Int> = MutableLiveData(0)
    val numberUnReadNotifyComment: LiveData<Int>
        get() = _numberUnReadNotifyComment


    fun countNumberUnReadNotifyOrder(it: NotifyOrderPagingAdapter) {
        _numberUnReadNotifyOrder.value = it.snapshot().count { notifyOrder ->
            var valid = false
            if (notifyOrder != null &&
                InfoUser.currentUser?.readAllOrderNoti?.before(notifyOrder.updatedAt) == true &&
                !notifyOrder.receiverIds[0].readState
            ) {
                valid = true
            }
            valid
        }
    }

    fun minusOneDigitCountUnReadNotifyOrder() {
        _numberUnReadNotifyOrder.value = numberUnReadNotifyOrder.value?.minus(1)
    }

    fun clearCountUnReadNotifyOrder() {
        _numberUnReadNotifyOrder.value = 0
    }

    fun countNumberUnReadNotifyComment(it: NotifyCommentPagingAdapter) {
        _numberUnReadNotifyComment.value = it.snapshot().count { notifyComment ->
            var valid = false
            if (notifyComment != null &&
                InfoUser.currentUser?.readAllCommentNoti?.before(notifyComment.updatedAt) == true &&
                !notifyComment.receiverIds[0].readState
            ) {
                valid = true
            }
            valid
        }
    }

    fun minusOneDigitCountUnReadNotifyComment() {
        _numberUnReadNotifyComment.value = numberUnReadNotifyOrder.value?.minus(1)
    }

    fun clearCountUnReadNotifyComment() {
        _numberUnReadNotifyComment.value = 0
    }
}
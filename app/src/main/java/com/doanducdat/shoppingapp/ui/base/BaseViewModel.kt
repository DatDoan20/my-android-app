package com.doanducdat.shoppingapp.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun handlerException(TAG: String, msg: String): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "$msg : ${throwable.message}")
        }
    }
}
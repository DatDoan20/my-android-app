package com.doanducdat.shoppingapp.ui.base

import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.validation.ResCatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {
    /***
     * errMsg to show in logcat, errObject to use catch err object from server
     */
    val loading = DataState.loading(null)
    val IO = Dispatchers.IO
    val token = InfoUser.localToken.toString()
    val str = AppConstants.QueryRequest

    fun <T> safeThreadDefaultCatch(
        callBack: Flow<DataState<T>>,
        scope: CoroutineDispatcher
    ): Flow<DataState<T>> {
        return callBack.catch { t ->
            emit(DataState.error(null, ResCatch.errMsg(t)))
        }.flowOn(scope)
    }

    fun <T> safeThreadNonCatch(
        callBack: Flow<DataState<T>>,
        scope: CoroutineDispatcher,
    ): Flow<DataState<T>> {
        return callBack.flowOn(scope)
    }
}
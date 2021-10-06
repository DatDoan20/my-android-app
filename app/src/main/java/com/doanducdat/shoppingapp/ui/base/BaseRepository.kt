package com.doanducdat.shoppingapp.ui.base

import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResCatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

open class BaseRepository {
    /***
     * errMsg to show in logcat, errObject to use catch err object from server
     */
    val loading = DataState.loading(null)

    fun <T> safeThreadDefaultCatch(
        call: Flow<DataState<T>>,
        scope: CoroutineDispatcher
    ): Flow<DataState<T>> {
        return call.catch { t ->
            emit(DataState.error(null, ResCatch.errMsg(t)))
        }.flowOn(scope)
    }

    fun <T> safeThreadNonCatch(
        call: Flow<DataState<T>>,
        scope: CoroutineDispatcher,
    ): Flow<DataState<T>> {
        return call.flowOn(scope)
    }
}
package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseAuth
import com.doanducdat.shoppingapp.model.user.Email
import com.doanducdat.shoppingapp.model.user.UserSignIn
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.validation.ResCatch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInRepository @Inject constructor(
    private val userAPI: UserAPI
) : BaseRepository() {

    suspend fun signInUser(userSignIn: UserSignIn) = safeThreadNonCatch(
        flow {
            emit(loading)
            val responseAuthSignInUser: ResponseAuth = userAPI.signInUser(userSignIn)
            emit(DataState.success(responseAuthSignInUser))
        }.catch { e ->
            emit(DataState.error(ResCatch.errResponseSignIn(e), ResCatch.errMsg(e)))
        }, IO
    )


    suspend fun updateEmail(email: Email) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseUpdateEmail = userAPI.updateEmail(InfoUser.localToken.toString(), email)
            emit(DataState.success(responseUpdateEmail))
        }, IO
    )

    suspend fun updateMe() = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseUpdateEmail = userAPI.updateMe(InfoUser.localToken.toString())
            emit(DataState.success(responseUpdateEmail))
        }, IO
    )

    suspend fun loadMe() = safeThreadNonCatch(
        flow {
//            Log.e(AppConstants.TAG.LOAD_ME, "repo1: ${Thread.currentThread().name}") (IO)
            emit(loading)
            val myInfo = userAPI.loadMe(InfoUser.localToken.toString())
            emit(DataState.success(myInfo))
        }.catch { t ->
//            Log.e(AppConstants.TAG.LOAD_ME, "repo2: ${Thread.currentThread().name}") (IO)
            emit(DataState.error(ResCatch.errResponseUser(t), ResCatch.errMsg(t)))
        }, IO
    )
}
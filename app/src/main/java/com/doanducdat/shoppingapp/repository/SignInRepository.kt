package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseAuth
import com.doanducdat.shoppingapp.model.user.Email
import com.doanducdat.shoppingapp.model.user.UserSignIn
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.handler.HandlerErrRes
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
            emit(DataState.error(HandlerErrRes.errResponseSignIn(e), HandlerErrRes.errMsg(e)))
        }, IO
    )


    suspend fun updateEmail(email: Email) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseUpdateEmail = userAPI.updateEmail(token, email)
            emit(DataState.success(responseUpdateEmail))
        }, IO
    )

    suspend fun updateMe(
        name: RequestBody,
        birthYear: RequestBody,
        sex: RequestBody,
        avatar: MultipartBody.Part?
    ) = safeThreadDefaultCatch(
        flow {
            emit(loading)
            val responseUser = if (avatar == null) {
                userAPI.updateMe(token, name, birthYear, sex)
            } else {
                userAPI.updateMeWithAvatar(token, name, birthYear, sex, avatar)
            }
            emit(DataState.success(responseUser))
        }, IO
    )

    suspend fun loadMe() = safeThreadNonCatch(
        flow {
//            Log.e(AppConstants.TAG.LOAD_ME, "repo1: ${Thread.currentThread().name}") (IO)
            emit(loading)
            val myInfo = userAPI.loadMe(InfoLocalUser.localToken.toString())
            emit(DataState.success(myInfo))
        }.catch { t ->
//            Log.e(AppConstants.TAG.LOAD_ME, "repo2: ${Thread.currentThread().name}") (IO)
            emit(DataState.error(HandlerErrRes.errResponseUser(t), HandlerErrRes.errMsg(t)))
        }, IO
    )
}
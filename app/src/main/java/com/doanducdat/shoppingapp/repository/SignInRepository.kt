package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.module.response.ResponseAuth
import com.doanducdat.shoppingapp.module.user.UserSignIn
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInRepository @Inject constructor(
    private val userAPI: UserAPI
) {

    suspend fun signInUser(userSignIn: UserSignIn) = flow {
        emit(DataState.loading(null))
        try {
            val responseAuthSignInUser: ResponseAuth = userAPI.signInUser(userSignIn)
            emit(DataState.success(responseAuthSignInUser))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
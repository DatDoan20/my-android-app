package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.module.response.ResponseAuth
import com.doanducdat.shoppingapp.module.response.ResponseUpdateEmail
import com.doanducdat.shoppingapp.module.response.ResponseUser
import com.doanducdat.shoppingapp.module.user.Email
import com.doanducdat.shoppingapp.module.user.UserSignIn
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.utils.InfoUser
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

    suspend fun updateEmail(email: Email) = flow {
        emit(DataState.loading(null))
        try {
            val responseUpdateEmail: ResponseUpdateEmail =
                userAPI.updateEmail(InfoUser.token.toString(), email)
            emit(DataState.success(responseUpdateEmail))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

    suspend fun loadMe() = flow {
        emit(DataState.loading(null))
        try {
            val myInfo: ResponseUser = userAPI.loadMe(InfoUser.token.toString())
            emit(DataState.success(myInfo))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
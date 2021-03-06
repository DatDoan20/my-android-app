package com.doanducdat.shoppingapp.repository

import android.app.Activity
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.user.UserSignUp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.ui.base.BaseRepository
import com.doanducdat.shoppingapp.utils.auth.PhoneAuthentication
import com.doanducdat.shoppingapp.utils.handler.HandlerErrRes
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val userAPI: UserAPI,
    private val phoneAuthentication: PhoneAuthentication
) : BaseRepository() {

    fun generateOTP(
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackResultGenerateOTP: MyPhoneAuth.ResultGenerateOTP,
        activity: Activity,
    ) {
        val callbackVerification =
            generateCallbacksVerification(
                phoneNumberWithCountryCode,
                phoneNumber,
                callbackResultGenerateOTP
            )
        phoneAuthentication.generateOTP(phoneNumberWithCountryCode, activity, callbackVerification)
    }

    private fun generateCallbacksVerification(
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackWhenCodeSent: MyPhoneAuth.ResultGenerateOTP,
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return phoneAuthentication.generateCallbacksVerification(
            phoneNumberWithCountryCode,
            phoneNumber,
            callbackWhenCodeSent
        )
    }

    fun verifyOTP(
        verificationID: String,
        otp: String,
        activity: Activity,
        callbackVerifyOTP: MyPhoneAuth.VerifyOTP
    ) {
        phoneAuthentication.verifyOTP(verificationID, otp, activity, callbackVerifyOTP)
    }

    suspend fun signUpUser(userSignUp: UserSignUp) = safeThreadNonCatch(
        flow {
            emit(loading)
            val responseAuthSignUpUser = userAPI.signUpUser(userSignUp)
            emit(DataState.success(responseAuthSignUpUser))
        }.catch { e ->
            emit(DataState.error(HandlerErrRes.errResponseSignUp(e), HandlerErrRes.errMsg(e)))
        }, IO
    )
}
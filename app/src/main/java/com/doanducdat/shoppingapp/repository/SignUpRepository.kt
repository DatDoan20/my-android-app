package com.doanducdat.shoppingapp.repository

import android.app.Activity
import com.doanducdat.shoppingapp.module.response.ResponseAuth
import com.doanducdat.shoppingapp.module.user.UserSignUp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.utils.PhoneAuthentication
import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val userAPI: UserAPI,
    private val phoneAuthentication: PhoneAuthentication
) {

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

    suspend fun signUpUser(userSignUp: UserSignUp) = flow {
        emit(DataState.loading(null))
        try {
            val responseAuthSignUpUser: ResponseAuth = userAPI.signUpUser(userSignUp)
            emit(DataState.success(responseAuthSignUpUser))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }

}
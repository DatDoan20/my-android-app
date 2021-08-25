package com.doanducdat.shoppingapp.repository

import android.app.Activity
import com.doanducdat.shoppingapp.module.Response
import com.doanducdat.shoppingapp.module.UserSignUp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.PhoneAuthentication
import com.doanducdat.shoppingapp.utils.response.DataState
import com.doanducdat.shoppingapp.utils.validation.ResponseValidation
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val userAPI: UserAPI
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
        PhoneAuthentication.generateOTP(phoneNumberWithCountryCode, activity, callbackVerification)
    }

    private fun generateCallbacksVerification(
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackWhenCodeSent: MyPhoneAuth.ResultGenerateOTP,
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return PhoneAuthentication.generateCallbacksVerification(
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
        PhoneAuthentication.verifyOTP(verificationID, otp, activity, callbackVerifyOTP)
    }

    suspend fun signUpUser(userSignUp: UserSignUp) = flow {
        emit(DataState.loading(null))
        try {
            val responseSignUpUser: Response = userAPI.signUpUser(userSignUp)
            emit(DataState.success(responseSignUpUser))
        } catch (e: Throwable) {
            emit(DataState.error(null, ResponseValidation.msgErrResponse(e)))
        }
    }
}
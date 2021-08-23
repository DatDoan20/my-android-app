package com.doanducdat.shoppingapp.repository

import android.app.Activity
import android.content.Context
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.retrofit.UserAPI
import com.doanducdat.shoppingapp.utils.PhoneAuthentication
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val userAPI: UserAPI
) {

    fun generateOTP(
        context: Context,
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackWhenCodeSent: MyPhoneAuth.WhenCodeSent,
        activity: Activity,
    ) {
        val callbackVerifyOTP =
            generateCallbacksVerification(
                context,
                phoneNumberWithCountryCode,
                phoneNumber,
                callbackWhenCodeSent
            )
        PhoneAuthentication.generateOTP(phoneNumberWithCountryCode, activity, callbackVerifyOTP)
    }

    private fun generateCallbacksVerification(
        context: Context,
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackWhenCodeSent: MyPhoneAuth.WhenCodeSent,
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return PhoneAuthentication.generateCallbacksVerification(
            context,
            phoneNumberWithCountryCode,
            phoneNumber,
            callbackWhenCodeSent
        )
    }

    fun verifyOTP(
        verificationID: String,
        otp: String,
        activity: Activity,
        context: Context,
        callbackVerifyOTP: MyPhoneAuth.VerifyOTP
    ) {
        PhoneAuthentication.verifyOTP(verificationID, otp, activity, context, callbackVerifyOTP)
    }
}
package com.doanducdat.shoppingapp.utils

import android.app.Activity
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

object PhoneAuthentication {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun generateOTP(
        phoneNumberWithCountryCode: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        auth.setLanguageCode(AppConstants.PhoneAuth.LANGUAGE_CODE_VN);
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumberWithCountryCode)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /*** callback function to listen verification :
     * Fail -> show message in toast
     * OTP was sent -> handle by callback function
     * */
    fun generateCallbacksVerification(
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackResultGenerateOTP: MyPhoneAuth.ResultGenerateOTP
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(error: FirebaseException) {
                var msg = AppConstants.MsgError.GENERIC_ERR_MSG
                when (error) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        msg = AppConstants.PhoneAuth.INVALID_CREDENTIALS
                    }
                    is FirebaseTooManyRequestsException -> {
                        msg = AppConstants.PhoneAuth.TOO_MANY_REQUESTS
                    }
                }
                callbackResultGenerateOTP.onCodeSentFailed(msg)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                val msg = AppConstants.PhoneAuth.CODE_SENT_SUCCESS
                callbackResultGenerateOTP.onCodeSentSuccess(
                    verificationId,
                    phoneNumberWithCountryCode,
                    phoneNumber,
                    msg
                )
            }
        }
    }

    /*** callback function to verify OTP after code OTP sent to client success */
    fun verifyOTP(
        verificationId: String,
        otp: String,
        activity: Activity,
        callbackVerifyOTP: MyPhoneAuth.VerifyOTP
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        auth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                callbackVerifyOTP.onVerifySuccess()
            } else {
                val msg = if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    AppConstants.PhoneAuth.OTP_ERR_MSG_UN_VALID
                } else {
                    AppConstants.MsgError.GENERIC_ERR_MSG
                }
                callbackVerifyOTP.onVerifyFailed(msg)
            }

        }
    }
}
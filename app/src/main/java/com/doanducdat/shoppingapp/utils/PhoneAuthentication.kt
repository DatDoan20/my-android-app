package com.doanducdat.shoppingapp.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
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
        context: Context,
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackWhenCodeSent: MyPhoneAuth.WhenCodeSent
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(error: FirebaseException) {
                val msgErr = when (error) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        AppConstants.PhoneAuth.INVALID_CREDENTIALS
                    }
                    is FirebaseTooManyRequestsException -> {
                        AppConstants.PhoneAuth.INVALID_CREDENTIALS
                    }
                    else -> {
                        AppConstants.MsgError.GENERIC_ERR_MSG
                    }
                }
                Toast.makeText(context, msgErr, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                callbackWhenCodeSent.setWhenCodeSent(
                    verificationId,
                    phoneNumberWithCountryCode,
                    phoneNumber
                )
            }
        }
    }

    /*** callback function to verify OTP after code OTP sent to client success */
    fun verifyOTP(
        verificationId: String,
        otp: String,
        activity: Activity,
        context: Context,
        callbackVerifyOTP: MyPhoneAuth.VerifyOTP
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        auth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            var msgResult = AppConstants.PhoneAuth.VERIFY_OTP_MSG
            if (task.isSuccessful) {
                Toast.makeText(context, msgResult, Toast.LENGTH_SHORT).show()
                callbackVerifyOTP.setVerifyOTP()
            } else {
                msgResult = if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    AppConstants.PhoneAuth.INVALID_CREDENTIALS
                } else {
                    AppConstants.MsgError.GENERIC_ERR_MSG
                }
                Toast.makeText(context, msgResult, Toast.LENGTH_SHORT).show()
            }

        }
    }
}
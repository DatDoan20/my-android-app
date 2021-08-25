package com.doanducdat.shoppingapp.myinterface

interface MyPhoneAuth {

    interface ResultGenerateOTP {
        /*** Generate OTP success */
        fun onCodeSentSuccess(
            verificationId: String,
            phoneNumberWithCountryCode: String,
            phoneNumber: String,
            msg: String
        )
        fun onCodeSentFailed(msg:String)
    }
    interface VerifyOTP{
        fun onVerifySuccess()
        fun onVerifyFailed(msg: String)
    }
}
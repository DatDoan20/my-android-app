package com.doanducdat.shoppingapp.myinterface

interface MyPhoneAuth {

    interface WhenCodeSent {
        /*** Generate OTP success */
        fun setWhenCodeSent(
            verificationId: String,
            phoneNumberWithCountryCode: String,
            phoneNumber: String
        )
    }
    interface VerifyOTP{
        fun setVerifyOTP()
    }
}
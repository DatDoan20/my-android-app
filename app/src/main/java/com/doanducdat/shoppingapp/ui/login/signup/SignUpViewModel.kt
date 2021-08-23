package com.doanducdat.shoppingapp.ui.login.signup

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doanducdat.shoppingapp.module.User
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.repository.SignUpRepository
import com.doanducdat.shoppingapp.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<User>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<User>>>
        get() = _dataState

    fun generateOTP(
        context: Context,
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackWhenCodeSent: MyPhoneAuth.WhenCodeSent,
        activity: Activity,
    ) {
        signUpRepository.generateOTP(
            context, phoneNumberWithCountryCode, phoneNumber, callbackWhenCodeSent, activity
        )
    }

    fun verifyOTP(
        verificationID: String,
        otp: String,
        activity: Activity,
        context: Context,
        callbackVerifyOTP: MyPhoneAuth.VerifyOTP
    ) {
        signUpRepository.verifyOTP(verificationID, otp, activity, context, callbackVerifyOTP)
    }

}
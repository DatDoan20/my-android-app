package com.doanducdat.shoppingapp.ui.login.signup

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.module.Response
import com.doanducdat.shoppingapp.module.UserSignUp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.repository.SignUpRepository
import com.doanducdat.shoppingapp.utils.response.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<Response>> = MutableLiveData()
    val dataState: LiveData<DataState<Response>>
        get() = _dataState

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun generateOTP(
        phoneNumberWithCountryCode: String,
        phoneNumber: String,
        callbackResultGenerateOTP: MyPhoneAuth.ResultGenerateOTP,
        activity: Activity,
    ) {
        signUpRepository.generateOTP(
            phoneNumberWithCountryCode, phoneNumber, callbackResultGenerateOTP, activity
        )
    }

    fun verifyOTP(
        verificationID: String,
        otp: String,
        activity: Activity,
        callbackVerifyOTP: MyPhoneAuth.VerifyOTP
    ) {
        signUpRepository.verifyOTP(verificationID, otp, activity, callbackVerifyOTP)
    }


    fun signUpUser(userSignUp:UserSignUp) = viewModelScope.launch {
        signUpRepository.signUpUser(userSignUp).onEach {
            _dataState.value = it
        }.launchIn(viewModelScope)
    }

}
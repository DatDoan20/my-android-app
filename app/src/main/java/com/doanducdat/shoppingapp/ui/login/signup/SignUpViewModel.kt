package com.doanducdat.shoppingapp.ui.login.signup

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseAuth
import com.doanducdat.shoppingapp.model.user.UserSignUp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<ResponseAuth>> = MutableLiveData()
    val dataState: LiveData<DataState<ResponseAuth>>
        get() = _dataState

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    var phone: String = ""
    var name: String = ""
    var password: String = ""
    var email: String = ""

    var stateErrPhone: Boolean = true
    var stateErrName: Boolean = true
    var stateErrEmail: Boolean = true
    var stateErrPassword: Boolean = true

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


    fun signUpUser() = viewModelScope.launch {
        val userSignUp: UserSignUp = UserSignUp(phone, name, password, email)
        signUpRepository.signUpUser(userSignUp).onEach {
            _dataState.value = it
        }.launchIn(viewModelScope)
    }
}
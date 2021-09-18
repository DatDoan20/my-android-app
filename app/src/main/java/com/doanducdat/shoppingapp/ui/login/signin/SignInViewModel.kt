package com.doanducdat.shoppingapp.ui.login.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.module.response.ResponseAuth
import com.doanducdat.shoppingapp.module.response.ResponseUser
import com.doanducdat.shoppingapp.module.user.UserSignIn
import com.doanducdat.shoppingapp.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {
    private val _dataStateUser: MutableLiveData<DataState<ResponseUser>> =
        MutableLiveData()
    val dataStateUser: LiveData<DataState<ResponseUser>>
        get() = _dataStateUser

    private val _dataState: MutableLiveData<DataState<ResponseAuth>> = MutableLiveData()
    val dataState: LiveData<DataState<ResponseAuth>>
        get() = _dataState

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    var phone: String = ""
    var password: String = ""

    var stateErrPhone: Boolean = true
    var stateErrPassword: Boolean = true

    fun signInUser() = viewModelScope.launch {
        val userSignIn: UserSignIn = UserSignIn(phone, password)
        signInRepository.signInUser(userSignIn).onEach {
            _dataState.value = it
        }.launchIn(viewModelScope)
    }

    fun loadMe() = viewModelScope.launch {
        signInRepository.loadMe().onEach {
            _dataStateUser.value = it
        }.launchIn(viewModelScope)
    }

}
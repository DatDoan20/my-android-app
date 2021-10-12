package com.doanducdat.shoppingapp.ui.main.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseUpdateEmail
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.model.user.Email
import com.doanducdat.shoppingapp.repository.SignInRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : BaseViewModel() {

    private val _dataStateUpdateEmail: MutableLiveData<DataState<ResponseUpdateEmail>> =
        MutableLiveData()
    val dataStateUpdateEmail: LiveData<DataState<ResponseUpdateEmail>>
        get() = _dataStateUpdateEmail

    private val _dataStateUpdateUser: MutableLiveData<DataState<ResponseUser>> =
        MutableLiveData()
    val dataStateUpdateUser: LiveData<DataState<ResponseUser>>
        get() = _dataStateUpdateUser

    // = false so it's not empty, it was assign name of user before
    var stateErrName = false
    var imageUri: Uri? = null
    var file: File? = null
    var name = ""
    var birthYear = ""
    var sex = ""

    fun updateEmail(email: Email) = viewModelScope.launch {
        signInRepository.updateEmail(email).onEach {
            _dataStateUpdateEmail.value = it
        }.launchIn(viewModelScope)
    }

    fun updateMe(
        name: RequestBody,
        birthYear: RequestBody,
        sex: RequestBody,
        avatar: MultipartBody.Part?
    ) = viewModelScope.launch {
        signInRepository.updateMe(name, birthYear, sex, avatar).onEach {
            _dataStateUpdateUser.value = it
        }.launchIn(viewModelScope)
    }
}
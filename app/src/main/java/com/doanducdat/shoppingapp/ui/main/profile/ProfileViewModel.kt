package com.doanducdat.shoppingapp.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseUpdateEmail
import com.doanducdat.shoppingapp.model.user.Email
import com.doanducdat.shoppingapp.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {
    private val _dataStateUpdateEmail: MutableLiveData<DataState<ResponseUpdateEmail>> =
        MutableLiveData()
    val dataStateUpdateEmail: LiveData<DataState<ResponseUpdateEmail>>
        get() = _dataStateUpdateEmail

    fun updateEmail(email: Email) = viewModelScope.launch {
        signInRepository.updateEmail(email).onEach {
            _dataStateUpdateEmail.value = it
        }.launchIn(viewModelScope)
    }
}
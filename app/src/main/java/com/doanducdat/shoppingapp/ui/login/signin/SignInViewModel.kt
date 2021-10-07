package com.doanducdat.shoppingapp.ui.login.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.model.response.DataState
import com.doanducdat.shoppingapp.model.response.ResponseAuth
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.model.user.UserSignIn
import com.doanducdat.shoppingapp.repository.SignInRepository
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.MyDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private var myDataStore: MyDataStore
) : BaseViewModel() {

    private val _dataStateUser: MutableLiveData<DataState<ResponseUser>> =
        MutableLiveData()
    val dataStateUser: LiveData<DataState<ResponseUser>>
        get() = _dataStateUser

    private val _dataStateSignIn: MutableLiveData<DataState<ResponseAuth>> = MutableLiveData()
    val dataStateSignIn: LiveData<DataState<ResponseAuth>>
        get() = _dataStateSignIn

    private val _token: MutableLiveData<String?> = MutableLiveData(null)
    val token: LiveData<String?>
        get() = _token

    var phone: String = ""
    var password: String = ""

    var stateErrPhone: Boolean = true
    var stateErrPassword: Boolean = true

    fun signInUser() = viewModelScope.launch {
        val userSignIn: UserSignIn = UserSignIn(phone, password)
        signInRepository.signInUser(userSignIn).onEach {
            _dataStateSignIn.value = it
        }.launchIn(viewModelScope)
    }


    /**If use coroutine -> run IO thread by using withContext
     *
     * but here flow -> use flowOn
     */
    fun loadMe() = viewModelScope.launch() {
        signInRepository.loadMe().onEach {
//            Log.e(AppConstants.TAG.LOAD_ME, "viewmodel: ${Thread.currentThread().name}") (main)
            _dataStateUser.value = it
        }.launchIn(viewModelScope)
    }

    fun getTokenLocal() = viewModelScope.launch {
        withContext(scopeIO) {
            _token.postValue(myDataStore.userJwtFlow.first().trim())
        }
    }

    fun writeTokenLocal(token: String) {
        val handlerException = handlerException(AppConstants.TAG.LOAD_ME, "writeJwt task")

        val writeTokenJob = viewModelScope.launch(handlerException + NonCancellable) {
            withContext(scopeIO) {
                myDataStore.writeJwt(token)
            }
        }
        writeTokenJob.invokeOnCompletion { handler ->
            if (handler == null) {
                InfoUser.localToken.append(token)
                Log.e(AppConstants.TAG.SIGN_IN, "ListenSignIn Success: ${InfoUser.localToken}")
                loadMe()
            }
        }
    }
}
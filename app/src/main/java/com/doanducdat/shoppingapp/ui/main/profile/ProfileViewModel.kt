package com.doanducdat.shoppingapp.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doanducdat.shoppingapp.module.cart.Cart
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.module.response.DataState
import com.doanducdat.shoppingapp.module.response.ResponseHandleProductInCart
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {
    var isVerifyEmailUser: MutableLiveData<Boolean> = MutableLiveData(false)

}
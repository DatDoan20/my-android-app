package com.doanducdat.shoppingapp.repository

import com.doanducdat.shoppingapp.retrofit.UserAPI
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val userAPI: UserAPI
){
}
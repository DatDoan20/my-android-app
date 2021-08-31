package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.ResponseAuth
import com.doanducdat.shoppingapp.module.user.UserSignIn
import retrofit2.http.Body
import retrofit2.http.GET


interface ProductAPI {


    @GET("/api/users/sign-in-user")
    suspend fun getProducts(): ResponseAuth
}
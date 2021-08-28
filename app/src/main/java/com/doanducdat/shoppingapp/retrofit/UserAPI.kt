package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.ResponseAuth
import com.doanducdat.shoppingapp.module.user.UserSignIn
import com.doanducdat.shoppingapp.module.user.UserSignUp
import retrofit2.http.Body
import retrofit2.http.POST


interface UserAPI {

    @POST("/api/users/sign-up")
    suspend fun signUpUser(@Body userSignUp: UserSignUp): ResponseAuth

    @POST("/api/users/sign-in-user")
    suspend fun signInUser(@Body userSignIn: UserSignIn): ResponseAuth
}
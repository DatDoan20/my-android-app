package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.Response
import com.doanducdat.shoppingapp.module.UserSignUp
import retrofit2.http.Body
import retrofit2.http.POST


interface UserAPI {
    @POST("/api/users/sign-up")
    suspend fun signUpUser(@Body userSignUp: UserSignUp): Response

}
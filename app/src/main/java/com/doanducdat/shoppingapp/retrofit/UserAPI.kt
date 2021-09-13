package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.cart.Cart
import com.doanducdat.shoppingapp.module.cart.Carts
import com.doanducdat.shoppingapp.module.response.ResponseAddToCart
import com.doanducdat.shoppingapp.module.response.ResponseAuth
import com.doanducdat.shoppingapp.module.user.UserSignIn
import com.doanducdat.shoppingapp.module.user.UserSignUp
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST


interface UserAPI {

    @POST("/api/users/sign-up")
    suspend fun signUpUser(@Body userSignUp: UserSignUp): ResponseAuth

    @POST("/api/users/sign-in-user")
    suspend fun signInUser(@Body userSignIn: UserSignIn): ResponseAuth

    /**
     *   status: 'success',
     *
     *   message: 'Add product to favorite list of user successfully',
     *
     *   data: infoUser,
     */
    @PATCH("api/users/add-to-cart")
    suspend fun addToCart(
        @Header("Authorization") authorization: String,
        @Body carts: Cart
    ): ResponseAddToCart
}
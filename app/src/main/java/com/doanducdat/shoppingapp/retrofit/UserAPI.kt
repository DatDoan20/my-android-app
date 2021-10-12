package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.ProductId
import com.doanducdat.shoppingapp.model.response.ResponseAuth
import com.doanducdat.shoppingapp.model.response.ResponseHandleProductInCart
import com.doanducdat.shoppingapp.model.response.ResponseUpdateEmail
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.model.user.Email
import com.doanducdat.shoppingapp.model.user.UserSignIn
import com.doanducdat.shoppingapp.model.user.UserSignUp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface UserAPI {
    @GET("/api/users/me")
    suspend fun loadMe(@Header("Authorization") authorization: String): ResponseUser

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
    ): ResponseHandleProductInCart


    @PATCH("api/users/update-email")
    suspend fun updateEmail(
        @Header("Authorization") authorization: String,
        @Body email: Email
    ): ResponseUpdateEmail

    @PATCH("api/users/delete-product-in-cart")
    suspend fun deleteProductInCart(
        @Header("Authorization") authorization: String,
        @Body ProductId: ProductId
    ): ResponseHandleProductInCart

    @Multipart
    @PATCH("api/users/update-me")
    suspend fun updateMeWithAvatar(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("birthYear") birthYear: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part file: MultipartBody.Part,
    ): ResponseUser

    @Multipart
    @PATCH("api/users/update-me")
    suspend fun updateMe(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("birthYear") birthYear: RequestBody,
        @Part("sex") sex: RequestBody,
    ): ResponseUser
}
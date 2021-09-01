package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.response.ResponseProduct
import retrofit2.http.GET
import retrofit2.http.Header


interface ProductAPI {


    @GET("api/products/search?limit=10")
    suspend fun getProducts(@Header("Authorization") authorization: String): ResponseProduct
}
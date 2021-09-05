package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.module.response.ResponseProduct
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ProductAPI {

    /**
     * All query default is descending Time -> query by newest
     * Discount default is 0 (not discount), query discount -> param discount is different: 0
     */
    @GET("api/products/search")
    suspend fun getProducts(
        @Header("Authorization") authorization: String,
        @Query("limit") limit:String ="",
        @Query("discount[ne]") discountDifferent:String ="",
    ): ResponseProduct


}
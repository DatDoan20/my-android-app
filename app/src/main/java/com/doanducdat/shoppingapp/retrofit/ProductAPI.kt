package com.doanducdat.shoppingapp.retrofit

import com.doanducdat.shoppingapp.model.response.ResponseProduct
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ProductAPI {

    /**
     * All query default is descending Time -> query by newest
     * param discountDifferent: 0 -> get product is discount
     * category: nam/nu
     * type: vay-suong/...
     */
    @GET("api/products/search")
    suspend fun getProducts(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("discount[ne]") discountDifferent: Int? = null,
        @Query("category") category: String? = null,
        @Query("type") type: String? = null,
        @Query("name") name: String? = null,
    ): ResponseProduct
}
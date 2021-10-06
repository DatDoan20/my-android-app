package com.doanducdat.shoppingapp.model.order

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * state, payment have value default, can null when post
 */
class Order(
    @SerializedName("_id")
    val id: String?,
    val addressDelivery: String,
    val costDelivery: Int,
    val emailUser: String,
    val nameUser: String,
    val paymentMode: String,
    val phoneUser: String,
    val state: String,
    val totalPayment: Int,
    val totalPrice: Int,
    val purchasedProducts: MutableList<PurchasedProduct>,
    val createdAt: Date?,
    val note:String
) : Serializable
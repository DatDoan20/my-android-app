package com.doanducdat.shoppingapp.model.order

import com.doanducdat.shoppingapp.utils.AppConstants

/**
 * (1) "finalPrice in here" is "price in cart" (when number increase,price increase as well)
 *
 * -> price * quantify
 *
 * "price in here" is price of one product item -> "price in cart" / "quantity in cart"
 *
 * (2) orderId not store in DB, store manual use when: review product in order,
 *
 * use orderId to update product in order
 */
class PurchasedProduct(
    val stateRating: Boolean,
    val color: String,
    val discount: Int,
    val finalPrice: Int,
    private val imageCover: String,
    val name: String,
    val price: Int,
    val productId: String,
    val quantity: Int,
    val size: String,
    var orderId:String?
) {
    fun getUrlImgCover(): String {
        return "${AppConstants.Server.PUBLIC_HOST}${AppConstants.LinkImg.PRODUCT}${productId}/${imageCover}"
    }
}
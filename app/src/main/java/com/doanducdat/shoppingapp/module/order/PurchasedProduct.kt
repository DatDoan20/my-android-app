package com.doanducdat.shoppingapp.module.order

import com.doanducdat.shoppingapp.utils.AppConstants

/**
 * "finalPrice in here" is "price in cart" (when number increase,price increase as well)
 *
 * -> price * quantify
 *
 * "price in here" is price of one product item -> "price in cart" / "quantity in cart"
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
    val size: String
) {
    fun getUrlImgCover(): String {
        return "${AppConstants.Server.HOST}${AppConstants.LinkImg.PRODUCT}${productId}/${imageCover}"
    }
}
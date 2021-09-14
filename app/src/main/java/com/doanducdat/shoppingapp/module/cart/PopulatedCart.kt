package com.doanducdat.shoppingapp.module.cart

import com.doanducdat.shoppingapp.module.product.Product
import java.text.DecimalFormat

/**
 * infoProduct is IdProduct
 */
 class PopulatedCart(
    val color: String,
    private val finalPrice: Int,
    val infoProduct: Product,
    val quantity: Int,
    val size: String
){
    fun getFinalPrice():String{
       val dec = DecimalFormat("#,###")
       return dec.format(this.finalPrice) + " đ"
    }
    fun getUnFormatFinalPrice():Int{
        return finalPrice
    }
    fun getFinalPriceUnDiscount(discount:Int): String {
        val finalPriceUnDiscount: Double = (finalPrice / ((100 - discount).toDouble() / 100))
        val dec = DecimalFormat("#,###")
        return dec.format(finalPriceUnDiscount) + " đ"
    }
 }
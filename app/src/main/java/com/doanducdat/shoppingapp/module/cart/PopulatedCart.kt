package com.doanducdat.shoppingapp.module.cart

import com.doanducdat.shoppingapp.module.product.Product
import java.text.DecimalFormat

/**
 * infoProduct is IdProduct and it was populated
 */
class PopulatedCart(
    val color: String,
    var finalPrice: Int,
    val infoProduct: Product,
    var quantity: Int,
    val size: String
) {

    fun getFormatFinalPrice(): String {
        val dec = DecimalFormat("#,###")
        return dec.format(this.finalPrice)
    }

    fun getFinalPriceUnDiscount(discount: Int): String {
        val finalPriceUnDiscount: Double = (finalPrice / ((100 - discount).toDouble() / 100))
        val dec = DecimalFormat("#,###")
        return dec.format(finalPriceUnDiscount) + " đ"
    }
}
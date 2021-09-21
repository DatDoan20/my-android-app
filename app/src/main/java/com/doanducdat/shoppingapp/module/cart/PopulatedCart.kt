package com.doanducdat.shoppingapp.module.cart

import com.doanducdat.shoppingapp.module.product.Product
import java.text.DecimalFormat

/**
 * infoProduct is IdProduct and it was populated
 */
class PopulatedCart(
    val color: String,
    var price: Int,
    val infoProduct: Product,
    var quantity: Int,
    val size: String
) {

    fun getFormatPrice(): String {
        val dec = DecimalFormat("#,###")
        return dec.format(this.price)
    }

    fun getPriceUnDiscount(discount: Int): String {
        val finalPriceUnDiscount: Double = (price / ((100 - discount).toDouble() / 100))
        val dec = DecimalFormat("#,###")
        return dec.format(finalPriceUnDiscount) + " đ"
    }

    fun getOneItemPrice(): Int {
        return price / quantity
    }
}
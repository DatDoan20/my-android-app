package com.doanducdat.shoppingapp.model.product

import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.DecimalFormat
import java.util.*

class Product(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    private val images: List<String>,
    private val imageCover: String,
    val slug: String,
    private val price: Int,
    val brand: String,
    val size: String,
    val color: String,
    val material: String,
    val pattern: String,
    private val discount: Int,
    val outOfStock: Boolean,
    val type: String,
    val category: String,
    val ratingsAverage: Float,
    private val ratingsQuantity: Int,
    val createdAt: Date
) : Serializable {
    fun getPrice(): String {
        val dec = DecimalFormat("#,###")
        return dec.format(this.price) + " đ"
    }

    fun getUnFormatPrice(): Int {
        return price
    }

    fun getPriceUnDiscount(): String {
        val priceUnDiscount: Double = (price / ((100 - discount).toDouble() / 100))
        val dec = DecimalFormat("#,###")
        return dec.format(priceUnDiscount) + " đ"
    }

    fun getDiscount(): String {
        return "-$discount%"
    }

    fun getUnFormatDiscount(): Int {
        return discount
    }

    fun getRatingsQuantity(): String {
        return "/${ratingsQuantity}"
    }

    fun getUnFormatRatingsQuantity(): Int {
        return ratingsQuantity
    }

    fun getImageCover(): String {
        return imageCover
    }

    fun getUrlImgCover(): String {
        return getUrlImg(imageCover)
    }

    fun getUrlImages(): MutableList<String> {
        val urlImagesProduct: MutableList<String> = mutableListOf()
        this.images.forEach {
            urlImagesProduct.add(getUrlImg(it))
        }
        return urlImagesProduct
    }

    fun getImages(): List<String> {
        return this.images
    }

    private fun getUrlImg(name: String): String {
        //imageCover and images are name of image, not link
        // thi fun return URL of image
        return "${AppConstants.Server.PUBLIC_HOST}${AppConstants.LinkImg.PRODUCT}${id}/${name}"
    }

}
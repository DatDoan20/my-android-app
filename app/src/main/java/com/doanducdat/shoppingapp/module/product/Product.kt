package com.doanducdat.shoppingapp.module.product

import com.doanducdat.shoppingapp.module.review.Review
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.DecimalFormat

class Product(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val images: List<String>,
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
    val ratingsQuantity: Int,
    val reviews: List<Review>
) : Serializable{
    fun getPrice(): String? {
        val dec = DecimalFormat("#,###")
        return dec.format(this.price)
    }

    fun getDiscount(): String {
        return "-$discount%"
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

    private fun getUrlImg(name: String): String {
        //imageCover and images are name of image, not link
        // thi fun return URL of image
        return "${AppConstants.Server.HOST}${AppConstants.LinkImg.PRODUCT}${id}/${name}"
    }
}
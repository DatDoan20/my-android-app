package com.doanducdat.shoppingapp.model.product

class ProductColor(private val hexCode: String, val name: String) {
    fun getHexColor(): String {
        return "#${hexCode.trim()}"
    }
}

package com.doanducdat.shoppingapp.module.product

class ProductColor(private val hexCode: String, val name: String) {
    fun getHexColor(): String {
        return "#$hexCode"
    }
}

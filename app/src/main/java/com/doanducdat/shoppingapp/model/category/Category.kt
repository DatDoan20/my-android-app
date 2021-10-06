package com.doanducdat.shoppingapp.model.category

import java.io.Serializable

data class Category(val ImageResource: Int, val category: String, val type: String, val name: String) :
    Serializable {
}
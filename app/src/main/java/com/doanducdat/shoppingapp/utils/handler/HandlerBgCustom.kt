package com.doanducdat.shoppingapp.utils.handler

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.doanducdat.shoppingapp.R

class HandlerBgCustom {
    companion object {
        @Volatile
        private var instance: HandlerBgCustom? = null

        fun getInstance(): HandlerBgCustom {
            if (instance == null) {
                instance = HandlerBgCustom()
            }
            return instance!!
        }
    }

    fun bgOvalStroke(context: Context, colorHex: String, widthStroke: Int): GradientDrawable {
        val bgItem = GradientDrawable()
        bgItem.shape = GradientDrawable.OVAL
        bgItem.setColor(Color.parseColor(colorHex))
        bgItem.setStroke(
            widthStroke,
            ContextCompat.getColor(context, R.color.colorProductPicked)
        )
        return bgItem
    }

    fun bgOval(colorHex: String): GradientDrawable {
        val bgItem = GradientDrawable()
        bgItem.shape = GradientDrawable.OVAL
        bgItem.setColor(Color.parseColor(colorHex))
        return bgItem
    }

    fun bgRadiusSize(context: Context, colorResource: Int): GradientDrawable {
        val bgItem = GradientDrawable()
        bgItem.setColor(ContextCompat.getColor(context, colorResource))
        bgItem.shape = GradientDrawable.RECTANGLE
        bgItem.cornerRadius = 13F
        return bgItem
    }

    fun bgRadiusStroke(context: Context, widthStroke: Int): GradientDrawable {
        val bgItem = GradientDrawable()
        bgItem.setStroke(
            widthStroke,
            ContextCompat.getColor(context, R.color.bgColorBtnGeneric)
        )
        bgItem.cornerRadius = 13F
        return bgItem
    }
}
package com.doanducdat.shoppingapp.utils.handler

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.doanducdat.shoppingapp.R

class HandlerViewState(val context: Context) {
    private val readBlackColor = ContextCompat.getColor(context, R.color.readBlack)
    private val readBlueColor = ContextCompat.getColor(context, R.color.readBlue)

    fun setStateReadDot(imgBlueDotReadState: ImageView) {
        imgBlueDotReadState.visibility = View.GONE
    }

    fun setColorReadTextView(
        txtName: TextView,
        txtTimeComment: TextView,
        txtContentNotify: TextView
    ) {
        txtName.setTextColor(readBlackColor)
        txtTimeComment.setTextColor(readBlueColor)
        txtContentNotify.setTextColor(readBlackColor)
    }
}
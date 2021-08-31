package com.doanducdat.shoppingapp.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ChangeSizeAnimation(val view: View?, val fromWidth: Int, val toWidth: Int) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, transformation: Transformation?) {
        val newWidth: Int
        if (view!!.width != toWidth) {
            newWidth = (fromWidth + (toWidth - fromWidth) * interpolatedTime).toInt()
            view.layoutParams.width = newWidth
//            view.requestLayout()
        }
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}
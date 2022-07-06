package com.doanducdat.shoppingapp.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class GridSpacingItemDecoration(
    private var top: Int? = null,
    private var bottom: Int? = null,
    private var left: Int? = null,
    private var right: Int? = null,
    private var isVertical: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)

        if (position == 0 && isVertical) {

        } else {
            top?.let { outRect.top = dpToPixel(it) }
        }
        bottom?.let { outRect.bottom = dpToPixel(it) }
        left?.let { outRect.left = dpToPixel(it) }
        right?.let { outRect.right = dpToPixel(it) }
    }

    fun dpToPixel(value: Int): Int {
        return (value * Resources.getSystem().displayMetrics.density.toInt())
    }
}
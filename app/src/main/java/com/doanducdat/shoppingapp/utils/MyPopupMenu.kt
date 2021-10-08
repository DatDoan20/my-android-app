package com.doanducdat.shoppingapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import com.doanducdat.shoppingapp.R


class MyPopupMenu(val context: Context) {

    private var callbackMarkAsRead: () -> Unit = {}
    private var callbackDelete: () -> Unit = {}
    private var callbackMarkAsReadAll: () -> Unit = {}
    private var callbackDeleteAll: () -> Unit = {}

    fun setOnClickMarkAsRead(funCallback: () -> Unit) {
        callbackMarkAsRead = funCallback
    }

    fun setOnClickMarkAsReadAll(funCallback: () -> Unit) {
        callbackMarkAsReadAll = funCallback
    }

    fun setOnClickDelete(funCallback: () -> Unit) {
        callbackDelete = funCallback
    }

    fun setOnClickDeleteAll(funCallback: () -> Unit) {
        callbackDeleteAll = funCallback
    }

    fun showMenu(anchorView: TextView, @MenuRes menuRes: Int) {
        //POPUPMENU MUST BE IMPORTED ANDROIDX CAN ABLE TO SHOW ICON
        val popup = PopupMenu(context, anchorView)
        popup.menuInflater.inflate(menuRes, popup.menu)

        setClickItemMenu(popup)
        customMarginIcon(popup)

        popup.show()
    }

    @SuppressLint("RestrictedApi")
    private fun customMarginIcon(popup: PopupMenu) {
        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)

            val iconMarginPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                5.toFloat(),
                context.resources.displayMetrics
            ).toInt()

            for (item in menuBuilder.visibleItems) {
                if (item.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                    } else {
                        item.icon =
                            object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                    }
                }
            }
        }
    }

    private fun setClickItemMenu(popup: PopupMenu) {
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.option_mark_as_read -> {
                    callbackMarkAsRead.invoke()
                }
                R.id.option_delete_notification -> {
                    callbackDelete.invoke()
                }
                R.id.option_mark_as_read_all -> {
                    callbackMarkAsReadAll.invoke()
                }
                R.id.option_delete_all_notification -> {
                    callbackDeleteAll.invoke()
                }
            }
            true
        }
    }
}
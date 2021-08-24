package com.doanducdat.shoppingapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import com.doanducdat.shoppingapp.R


object MyDialog {
    private lateinit var dialog: Dialog

    fun build(context: Context, msgDialog: String, onClick: () -> Unit) {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.my_dialog)
        dialog.setCancelable(false)
        val window = dialog.window
        window?.let {
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        (dialog.findViewById(R.id.btn_try_again) as Button).setOnClickListener {
            dialog.dismiss()
            onClick()
        }
        (dialog.findViewById(R.id.txt_msg) as TextView).text = msgDialog
    }

    fun show() {
        dialog.show()
    }
}
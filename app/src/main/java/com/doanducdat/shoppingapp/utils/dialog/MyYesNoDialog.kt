package com.doanducdat.shoppingapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import com.doanducdat.shoppingapp.R


class MyYesNoDialog(context: Context) {
    private lateinit var dialog: Dialog
    private var callbackClickYes: () -> Unit = {}

    init {
        initDialog(context)
    }

    private fun initDialog(context: Context) {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.my_yes_no_dialog)
        dialog.setCancelable(false)
        val window = dialog.window
        dialog
        window?.let {
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val btnCancel: Button = dialog.findViewById(R.id.btn_cancel)
        val btnYes: Button = dialog.findViewById(R.id.btn_yes)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            callbackClickYes.invoke()
        }
    }

    fun setText(msgDialog: String) {
        val msgText: TextView = dialog.findViewById(R.id.txt_msg)
        msgText.text = msgDialog
    }

    fun show() {
        dialog.show()
    }
    fun dismiss(){
        dialog.dismiss()
    }

    fun mySetOnClickYes(funClickYes: () -> Unit) {
        callbackClickYes = funClickYes
    }
}
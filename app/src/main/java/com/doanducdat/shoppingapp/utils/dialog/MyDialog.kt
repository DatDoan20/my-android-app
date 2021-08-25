package com.doanducdat.shoppingapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.MyDialogBinding


class MyBasicDialog() {
    private lateinit var dialog: Dialog
    //use binding -> layout was broken
//    private lateinit var binding: MyDialogBinding

    fun initDialog(context: Context, msgDialog: String): Dialog {
//        binding = MyDialogBinding.inflate(LayoutInflater.from(context), null, false)
        dialog = Dialog(context)
        dialog.setContentView(R.layout.my_dialog)
        dialog.setCancelable(false)
        val window = dialog.window
        dialog
        window?.let {
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val msgText: TextView = dialog.findViewById(R.id.txt_msg)
        val btnTryAgain: Button = dialog.findViewById(R.id.btn_try_again)
        msgText.text = msgDialog
        btnTryAgain.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }
}
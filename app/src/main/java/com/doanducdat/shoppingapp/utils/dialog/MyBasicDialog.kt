package com.doanducdat.shoppingapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import com.doanducdat.shoppingapp.R


class MyBasicDialog(context: Context) {
    private lateinit var dialog: Dialog
    //use binding -> layout was broken
//    private late init var binding: MyDialogBinding

    init {
        initDialog(context)
    }

    private fun initDialog(context: Context) {
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
        val btnTryAgain: Button = dialog.findViewById(R.id.btn_try_again)
        btnTryAgain.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun setTextButton(msgButton: String){
        val btnTryAgain: Button = dialog.findViewById(R.id.btn_try_again)
        btnTryAgain.text = msgButton
    }
    fun setText(msgDialog: String) {
        val msgText: TextView = dialog.findViewById(R.id.txt_msg)
        msgText.text = msgDialog
    }

    fun show() {
        dialog.show()
    }
}
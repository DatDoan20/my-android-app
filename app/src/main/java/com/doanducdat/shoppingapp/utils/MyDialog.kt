package com.doanducdat.shoppingapp.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import com.doanducdat.shoppingapp.R


object MyDialog {
    private lateinit var dialog: Dialog

    fun build(activity: Activity, msgDialog:String,  onClick: () -> Unit) {
        dialog = Dialog(activity)
        dialog.setContentView(R.layout.my_dialog)
        dialog.setCancelable(false)
        val window = dialog.window
        window?.let {
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        (dialog.findViewById(R.id.btn_try_again) as Button).setOnClickListener {
            onClick()
        }
        (dialog.findViewById(R.id.txt_msg) as TextView).text = msgDialog
//        window.attributes.gravity = Gravity.CENTER
//        val windowAttributes = window!!.attributes
//        windowAttributes.gravity = gravity
//        window!!.attributes = windowAttributes
//        //set width and height
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT );
    }
    fun show(){
        dialog.show()
    }
    fun dismiss(){
        dialog.dismiss()
    }
}
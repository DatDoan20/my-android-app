package com.doanducdat.shoppingapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import com.doanducdat.shoppingapp.R
import com.github.ybq.android.spinkit.SpinKitView


class MyRatingDialog(context: Context) {
    private lateinit var dialog: Dialog
    private var callbackClickYes: () -> Unit = {}

    init {
        initDialog(context)
    }

    private fun initDialog(context: Context) {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.my_rating_dialog)
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


    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun mySetOnClickYes(funClickYes: () -> Unit) {
        callbackClickYes = funClickYes
    }

    fun getRatingValue(): Float {
        val ratingBar: RatingBar = dialog.findViewById(R.id.rating_bar)
        return ratingBar.rating
    }
    fun getReview(): String {
        val edtReview: EditText= dialog.findViewById(R.id.edt_content_review)
        return edtReview.text.toString()
    }

    fun setStateDialog(state: Boolean) {
        val btnCancel: Button = dialog.findViewById(R.id.btn_cancel)
        val btnYes: Button = dialog.findViewById(R.id.btn_yes)
        btnCancel.isEnabled = state
        btnYes.isEnabled = state
    }

    fun setStateProgressBar(state: Int) {
        val progressbar: SpinKitView =
            dialog.findViewById(R.id.spin_kit_progress_bar_my_rating_dialog)
        progressbar.visibility = state
    }

}
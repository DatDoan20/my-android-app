package com.doanducdat.shoppingapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import com.doanducdat.shoppingapp.R


class MyFilterDialog(val context: Context) {
    private lateinit var dialog: Dialog

    var callback: (fromPrice: String?, toPrice: String?, idRdoSort: Int) -> Unit = { _: String?, _: String?, _:Int -> }

    fun setOnClick(funCallback: (fromPrice: String?, toPrice: String?, idRdoSort: Int) -> Unit) {
        callback = funCallback
    }

    fun showDialogFilter() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.alert_filter)
        dialog.setCancelable(true)
        dialog.window?.let {
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val btnTryAgain: Button = dialog.findViewById(R.id.btn_apply)
        btnTryAgain.setOnClickListener {
            val fromPrice: String? =
                (dialog.findViewById<EditText>(R.id.edtFromPrice).text.toString()).ifEmpty { null }
            val toPrice: String? =
                (dialog.findViewById<EditText>(R.id.edtToPrice).text.toString()).ifEmpty { null }
            val idRdoSort = dialog.findViewById<RadioGroup>(R.id.rdo_group).checkedRadioButtonId
//            Log.d("TEST", "showDialogFilter: $idRdoSort ${R.id.rdo_newest}")
            dialog.dismiss()
            callback.invoke(fromPrice, toPrice, idRdoSort)
        }
        dialog.show()
    }

    fun hideDialogFilter() {
        dialog.dismiss()
    }


}
package com.doanducdat.shoppingapp.utils.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.EmailAuthentication
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser


class MyVerifyEmailDialog(val activity: Activity) {
    private lateinit var dialog: Dialog
    var stateEmailErr = true
    var isSendEmailVerifySuccess = false
    lateinit var user: FirebaseUser

    private var callbackListenUpdateEmailSuccess: (email: String) -> Unit = {}
    fun myOnListenUpdateEmail(funListen: (email: String) -> Unit) {
        callbackListenUpdateEmailSuccess = funListen
    }

    init {
        initDialog()
    }

    private fun initDialog() {
        dialog = Dialog(activity)
        dialog.setContentView(R.layout.my_verify_email_dialog)
        dialog.setCancelable(false)
        val window = dialog.window
        dialog
        window?.let {
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        checkValidationEmail(dialog)
        sendEmailVerify(dialog)
        updateEmailVerified(dialog)
        val btnClose: ImageView = dialog.findViewById(R.id.img_close)
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun checkValidationEmail(dialog: Dialog) {
        val email: TextInputEditText = dialog.findViewById(R.id.txt_input_edt_email_dialog)
        val emailLayout: TextInputLayout = dialog.findViewById(R.id.txt_input_layout_email_dialog)
        email.doAfterTextChanged {
            val errMsgEmail = FormValidation.checkValidationEmail(it.toString())
            stateEmailErr = errMsgEmail != null
            emailLayout.error = errMsgEmail
        }
    }

    private fun sendEmailVerify(dialog: Dialog) {
        val btnSend: Button = dialog.findViewById(R.id.btn_send)
        val email: TextInputEditText = dialog.findViewById(R.id.txt_input_edt_email_dialog)

        btnSend.setOnClickListener {
            if (stateEmailErr) {
                showMsg(AppConstants.MsgErr.EMAIL_ERR_MSG)
            } else {
                setStateProgressBar(View.VISIBLE)
                setStateDialog(false)
                //send letter to email
                val emailAuth: EmailAuthentication = EmailAuthentication(activity)
                emailAuth.mySetOnVerifyEmailSentSuccess {
                    user = it
                    isSendEmailVerifySuccess = true
                    setStateProgressBar(View.GONE)
                    setStateDialog(true)
                }
                emailAuth.verifyEmailUser(email.text.toString().trim())
            }
        }
    }

    private fun updateEmailVerified(dialog: Dialog) {
        val btnUpdateEmail: Button = dialog.findViewById(R.id.btn_update_email)
        btnUpdateEmail.setOnClickListener {
            if (!isSendEmailVerifySuccess) {
                showMsg(AppConstants.EmailAuth.NOT_SEND)
            } else {
                setStateProgressBar(View.VISIBLE)
                setStateDialog(false)
                //check if user is verify letter in Email
                user.reload().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!user.isEmailVerified) {
                            setStateProgressBar(View.GONE)
                            setStateDialog(true)
                            showMsg(AppConstants.EmailAuth.NOT_VERIFY)
                        } else {
                            //ok api update email
                            callbackListenUpdateEmailSuccess(user.email!!)
                        }
                    } else {
                        setStateProgressBar(View.GONE)
                        setStateDialog(true)
                        Log.d(AppConstants.TAG.VERIFY_EMAIL, "updateEmailVerified: reload failed")
                        showMsg(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                }
            }
        }
    }

    private fun showMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun setStateDialog(state: Boolean) {
        val btnClose: ImageView = dialog.findViewById(R.id.img_close)
        val btnSend: Button = dialog.findViewById(R.id.btn_send)
        val btnUpdate: Button = dialog.findViewById(R.id.btn_update_email)
        val edtInput: TextInputEditText = dialog.findViewById(R.id.txt_input_edt_email_dialog)
        edtInput.isEnabled = state
        btnClose.isEnabled = state
        btnSend.isEnabled = state
        btnUpdate.isEnabled = state
    }

    fun setStateProgressBar(state: Int) {
        val progressbar: SpinKitView =
            dialog.findViewById(R.id.spin_kit_progress_bar_verify_email_dialog)
        progressbar.visibility = state
    }

}
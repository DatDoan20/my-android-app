package com.doanducdat.shoppingapp.utils.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.EmailAuthentication
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser


class MyVerifyEmailDialog(val activity: Activity) {
    private lateinit var dialog: Dialog
    var stateEmailErr = true
    var isSendEmailVerifySuccess = false
    lateinit var user: FirebaseUser

    init {
        initDialog(activity)
    }

    private fun initDialog(activity: Activity) {
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
        sendEmailVerify(activity, dialog)
        updateEmailVerified(activity, dialog)

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

    private fun sendEmailVerify(activity: Activity, dialog: Dialog) {
        val btnSend: Button = dialog.findViewById(R.id.btn_send)
        val email: TextInputEditText = dialog.findViewById(R.id.txt_input_edt_email_dialog)

        btnSend.setOnClickListener {
            if (stateEmailErr) {
                showMsg(AppConstants.MsgErr.EMAIL_ERR_MSG)
            } else {
                //send letter to email
                val emailAuth: EmailAuthentication = EmailAuthentication()
                emailAuth.mySetOnVerifyEmailSentSuccess {
                    user = it
                    isSendEmailVerifySuccess = true
                }
                emailAuth.verifyEmailUser(email.text.toString().trim(), activity)
            }
        }
    }

    private fun updateEmailVerified(activity: Activity, dialog: Dialog) {
        val btnUpdateEmail: Button = dialog.findViewById(R.id.btn_update_email)
        btnUpdateEmail.setOnClickListener {
            if (!isSendEmailVerifySuccess) {
                showMsg(AppConstants.EmailAuth.NOT_SEND)
            } else {
                //check if user is verify letter in Email
                user.reload().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!user.isEmailVerified) {
                            showMsg(AppConstants.EmailAuth.NOT_VERIFY)
                        } else {
                            //ok api update email
                        }
                    } else {
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
}
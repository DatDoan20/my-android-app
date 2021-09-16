package com.doanducdat.shoppingapp.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailAuthentication() {
    private val auth: FirebaseAuth = Firebase.auth
    lateinit var user: FirebaseUser
    private val VERIFY_EMAIL = AppConstants.TAG.VERIFY_EMAIL
    companion object {
        @Volatile
        private var instance: EmailAuthentication? = null

        fun getInstance(): EmailAuthentication {
            if (instance == null) {
                instance = EmailAuthentication()
            }
            return instance!!
        }
    }

    var listenVerifyEmailSendSuccess: (user: FirebaseUser) -> Unit = {}

    fun mySetOnVerifyEmailSentSuccess(funListen: (user: FirebaseUser) -> Unit) {
        this.listenVerifyEmailSendSuccess = funListen
    }

    fun verifyEmailUser(email: String, activity: Activity) {
        createEmailAndPassword(email, activity)
    }

    /**
     * create email + password and then verify that email
     */
    private fun createEmailAndPassword(email: String, activity: Activity) {
        auth.createUserWithEmailAndPassword(email, AppConstants.EmailAuth.PASS_DEFAULT)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(VERIFY_EMAIL, "createUserWithEmail:success")
                    user = auth.currentUser!!
                    sendVerifyEmail(user, activity)
                } else {
                    Log.e(VERIFY_EMAIL, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        AppConstants.MsgErr.GENERIC_ERR_MSG,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun sendVerifyEmail(user: FirebaseUser?, activity: Activity) {
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(VERIFY_EMAIL, "sendVerifyEmail success.")
                    Toast.makeText(
                        activity,
                        AppConstants.EmailAuth.SEND_SUCCESS,
                        Toast.LENGTH_SHORT
                    ).show()
                    listenVerifyEmailSendSuccess(user)
                } else {
                    Log.e(VERIFY_EMAIL, "sendVerifyEmail ${task.exception}")
                    Toast.makeText(
                        activity,
                        AppConstants.MsgErr.GENERIC_ERR_MSG,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}
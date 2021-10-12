package com.doanducdat.shoppingapp.utils.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailAuthentication(val activity: Activity) {
    private val auth: FirebaseAuth = Firebase.auth
    lateinit var user: FirebaseUser
    private val VERIFY_EMAIL = AppConstants.TAG.VERIFY_EMAIL

    var listenVerifyEmailSendSuccess: (user: FirebaseUser) -> Unit = {}

    fun mySetOnVerifyEmailSentSuccess(funListen: (user: FirebaseUser) -> Unit) {
        this.listenVerifyEmailSendSuccess = funListen
    }

    fun verifyEmailUser(email: String) {
        createEmailAndPassword(email)
    }

    /**
     * create email + password and then verify that email
     */
    private fun createEmailAndPassword(email: String) {
        auth.createUserWithEmailAndPassword(email, AppConstants.EmailAuth.PASS_DEFAULT)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(VERIFY_EMAIL, "createUserWithEmail:success")
                    user = auth.currentUser!!
                    sendVerifyEmail(user)
                } else {
                    //email is exist already
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Log.d(VERIFY_EMAIL, "Email is exist: call send verify email")
                        signInWithEmailExist(email)
                    } else {
                        Log.e(VERIFY_EMAIL, "createUserWithEmail:failure", task.exception)
                        showMsg(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                }
            }
    }

    private fun signInWithEmailExist(email: String) {
        auth.signInWithEmailAndPassword(email, AppConstants.EmailAuth.PASS_DEFAULT)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(VERIFY_EMAIL, "signInWithEmail:success")
                    val user = auth.currentUser
                    sendVerifyEmail(user)
                } else {
                    Log.w(VERIFY_EMAIL, "signInWithEmail:failure", task.exception)
                    showMsg(AppConstants.MsgErr.GENERIC_ERR_MSG)
                }
            }
    }

    private fun sendVerifyEmail(user: FirebaseUser?) {
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(VERIFY_EMAIL, "sendVerifyEmail success.")
                    showMsg(AppConstants.EmailAuth.SEND_SUCCESS)
                    listenVerifyEmailSendSuccess(user)
                } else {
                    Log.e(VERIFY_EMAIL, "sendVerifyEmail ${task.exception}")
                    showMsg(AppConstants.MsgErr.GENERIC_ERR_MSG)
                }
            }
    }

    fun showMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}
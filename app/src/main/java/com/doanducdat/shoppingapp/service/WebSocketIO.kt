package com.doanducdat.shoppingapp.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.doanducdat.shoppingapp.module.review.NotifyComment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.HandlerNotification
import com.doanducdat.shoppingapp.utils.InfoUser
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URISyntaxException


object WebSocketIO {

    private var mSocket: Socket? = null

    @Synchronized
    fun initSocket() {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000")
        } catch (e: URISyntaxException) {
            Log.e(AppConstants.TAG.SOCKET_IO, "initSocket: ${e.message}")
            Log.e(AppConstants.TAG.SOCKET_IO, "initSocket: ${e.printStackTrace()}")
        }
    }

    //get and connect ini onCreate
    @Synchronized
    fun getSocket(): Socket? = this.mSocket

    fun emitSignIn() {
        if (mSocket == null) return
        Log.e(AppConstants.TAG.SOCKET_IO, "emitSignIn: ")
        mSocket!!.emit("ConnectLogin", InfoUser.currentUser?.id);
    }

    fun listenNotifyComment(context: Context) {
        if (mSocket == null) return
        mSocket!!.on("newComment") { args ->
            if (args[0] != null) {
                val gson = Gson()
                val notifyComment = gson.fromJson(args[0].toString(), NotifyComment::class.java)
//                Log.e(AppConstants.TAG.SOCKET_IO, "listenNotifyComment: $notifyComment", )
                CoroutineScope(Dispatchers.Main).launch {
                    HandlerNotification.sendNotification(context, notifyComment)
                }
            }

        }
    }

}
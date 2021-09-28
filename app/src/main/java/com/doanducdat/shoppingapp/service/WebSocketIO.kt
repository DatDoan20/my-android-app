package com.doanducdat.shoppingapp.service

import android.content.Context
import android.util.Log
import com.doanducdat.shoppingapp.module.order.NotifyOrder
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


class WebSocketIO {
    companion object {
        @Volatile
        private var instance: WebSocketIO? = null
        fun getInstance(): WebSocketIO {
            if (instance == null) {
                instance = WebSocketIO()
            }
            return instance!!
        }
    }

    private lateinit var handlerNotification: HandlerNotification
    private var mSocket: Socket? = null

    @Synchronized
    fun initSocket(context: Context) {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000")
            handlerNotification = HandlerNotification(context)
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

    fun listenNewNotifyComment() {
        if (mSocket == null) return
        mSocket!!.on(AppConstants.SocketIO.NEW_COMMENT) { args ->
            if (args[0] != null) {
                val gson = Gson()
                val notifyComment = gson.fromJson(args[0].toString(), NotifyComment::class.java)
//                Log.e(AppConstants.TAG.SOCKET_IO, "listenNotifyComment: $notifyComment", )
                CoroutineScope(Dispatchers.Main).launch {
                    handlerNotification.sendNewComment(notifyComment)
                }
            }

        }
    }

    fun listenStateNotifyOrder() {
        if (mSocket == null) return
        mSocket!!.on(AppConstants.SocketIO.STATE_ORDER) { args ->
            if (args[0] != null) {
                val gson = Gson()
                val notifyOrder = gson.fromJson(args[0].toString(), NotifyOrder::class.java)
//                Log.e(AppConstants.TAG.SOCKET_IO, "listenStateNotifyOrder: ${notifyOrder}", )
                CoroutineScope(Dispatchers.Main).launch {
                    handlerNotification.sendStateOrder(notifyOrder)
                }
            }

        }
    }

    fun offListenNewNotifyComment() {
        if (mSocket == null) return
        mSocket!!.off(AppConstants.SocketIO.NEW_COMMENT)
    }

    fun offListenStateNotifyOrder() {
        if (mSocket == null) return
        mSocket!!.off(AppConstants.SocketIO.STATE_ORDER)
    }

}
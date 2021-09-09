package com.doanducdat.shoppingapp.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

class NetworkMonitorUtil(val context: Context) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val callbackNetworkAndroid9AndAbove = callbackNetworkAndroid9AndAbove()
    private val callbackNetworkAndroid8AndBelow = callbackNetworkAndroid8AndBelow()

    /**
     * true -> available connect internet
     *
     * false -> unavailable connect internet
     */
    lateinit var result: ((isAvailable: Boolean) -> Unit)

    fun register() {
        //ANDROID 9 AND ABOVE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //(1) check current network
            if (cm.activeNetwork == null) {
                // UNAVAILABLE
                result(false)
            }
            // (2) check network change
            cm.registerDefaultNetworkCallback(callbackNetworkAndroid9AndAbove)
        }
        //ANDROID 8 AND BELOW
        else {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            context.registerReceiver(callbackNetworkAndroid8AndBelow, intentFilter)
        }
    }


    private fun callbackNetworkAndroid9AndAbove(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                // UNAVAILABLE
                result(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    // WIFI
                    result(true)
                } else {
                    // CELLULAR
                    result(true)
                }
            }
        }
    }

    private fun callbackNetworkAndroid8AndBelow(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (cm.activeNetworkInfo == null) {
                    // UNAVAILABLE
                    result(false)
                    return
                }

                if (cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI) {
                    // WIFI
                    result(true)
                } else {
                    // CELLULAR
                    result(true)
                }
            }
        }
    }

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(callbackNetworkAndroid9AndAbove)
        } else {
            context.unregisterReceiver(callbackNetworkAndroid8AndBelow)
        }
    }

}
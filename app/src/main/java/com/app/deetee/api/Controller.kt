package com.app.deetee.api

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class Controller {

    companion object {
        @JvmStatic
        fun isOnline(context: Activity): Boolean {
            return try {
                val connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.getSystemService(ConnectivityManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                }

                if (connectivityManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val network = connectivityManager.activeNetwork
                        val capabilities = connectivityManager.getNetworkCapabilities(network)
                        capabilities != null &&
                                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    } else {
                        @Suppress("DEPRECATION")
                        val activeNetworkInfo = connectivityManager.activeNetworkInfo
                        @Suppress("DEPRECATION")
                        activeNetworkInfo != null && activeNetworkInfo.isConnected &&
                                (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
                                        || activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE)
                    }
                } else {
                    false
                }
            } catch (e: Exception) {
                println("CheckConnectivity Exception: ${e.message}")
                false
            }
        }
    }
}

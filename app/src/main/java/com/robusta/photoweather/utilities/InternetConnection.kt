package com.robusta.photoweather.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import com.robusta.photoweather.PhotoWeatherApplication.Companion.mApplication

object InternetConnection {

    fun hasInternetConnection(): Boolean {

        val connectivityManager =
            mApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

            capabilities?.let { netCap ->
                when {
                    netCap.hasTransport(TRANSPORT_WIFI) -> return true
                    netCap.hasTransport(TRANSPORT_CELLULAR) -> return true
                    netCap.hasTransport(TRANSPORT_ETHERNET) -> return true
                    else -> return false
                }
            }

        } else {
            connectivityManager.activeNetworkInfo?.run {
                when (type) {
                    TYPE_WIFI -> return true
                    TYPE_MOBILE -> return true
                    TYPE_ETHERNET -> return true
                    else -> return false
                }
            }
        }
        return false
    }

}
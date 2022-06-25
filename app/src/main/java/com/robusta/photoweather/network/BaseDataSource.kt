package com.robusta.photoweather.network

import android.util.Log
import com.google.gson.Gson
import com.robusta.photoweather.utilities.InternetConnection.hasInternetConnection
import com.robusta.photoweather.utilities.Resource
import retrofit2.Response
import java.io.IOException


@Suppress("LiftReturnOrAssignment")
abstract class BaseDataSource {

    companion object {
        const val TAG = "BaseDataSource"
    }


    // info : safe api call
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        try {

            if (hasInternetConnection()) {

                val response = apiCall()

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d(TAG, "safeApiCall: body >>> $body")

                    if (body != null) {
                        return Resource.success(body)
                    }

                } else {

                    val gson = Gson().fromJson(response.errorBody()?.charStream(), ErrorBody::class.java)
                    return Resource.error(gson.message)

                }

                return Resource.failed("Something went wrong, try again")


            } else {

                return Resource.failed("No Internet Connection")

            }


        } catch (t: Throwable) {

            when (t) {
                is IOException -> return Resource.failed("Network Failure")
                else -> {
                    Log.e(TAG, "Conversion Error ${t.stackTraceToString()}")
                    return Resource.failed("Conversion Error")
                }
            }

        }
    }

    // info : safe api call paging
    suspend fun <T> safeApiCallPaging(pagingLogic:(T) -> Resource<T> ,apiCall: suspend () -> Response<T>): Resource<T> {
        try {

            if (hasInternetConnection()) {

                val response = apiCall()

                if (response.isSuccessful) {


                    response.body()?.let { resultRes ->
                        return pagingLogic(resultRes)
                        // return Resource.success(cashedResponse ?: resultRes)
                    }

                } else {

                    val gson = Gson().fromJson(response.errorBody()?.charStream(), ErrorBody::class.java)
                    return Resource.error(gson.message)

                }

                return Resource.failed("Something went wrong, try again")


            } else {

                return Resource.failed("No Internet Connection")

            }


        } catch (t: Throwable) {

            when (t) {
                is IOException -> return Resource.failed("Network Failure")
                else -> {
                    Log.e(TAG, "Conversion Error ${t.stackTraceToString()}")
                    return Resource.failed("Conversion Error")
                }
            }

        }
    }

    // info: Error Body Model
    data class ErrorBody(
        val message: String,
    )

}
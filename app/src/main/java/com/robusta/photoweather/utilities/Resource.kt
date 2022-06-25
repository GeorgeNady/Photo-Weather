package com.robusta.photoweather.utilities

import android.util.Log

data class Resource<T>(
    val success: Status,
    val data: T? = null,
    val message: String? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        FAILURE;

        fun isLoading() = this == LOADING
    }

    companion object {

        private const val TAG = "Resource"

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null)
        }

        fun <T> failed(message: String, data: T? = null): Resource<T> {
            return Resource(Status.FAILURE, data, message)
        }

    }

    fun handler(
        mLoading: (() -> Unit)? = null,
        mError: ((String) -> Unit)? = null,
        mFailed: ((String) -> Unit)? = null,
        mSuccess: (T) -> Unit,
    ) {
        when (this.success) {
            Status.LOADING -> {
                mLoading?.let { mLoading() }
                Log.d(TAG, "$TAG >>> LOADING")
            }
            Status.ERROR -> {
                mError?.let { mError(message!!) }
                Log.d(TAG, "$TAG >>> ERROR $message")
            }
            Status.FAILURE -> {
                mFailed?.let { mFailed(message!!) }
                Log.d(TAG, "$TAG >>> FAILURE $message")
            }
            Status.SUCCESS -> {
                mSuccess(data!!)
                Log.d(TAG, "$TAG >>> SUCCESS $data")
            }
        }
    }
    /*
    x.handler(
        mLoading = {},
        mError = {},
        mFailed = {},
     ) {
           TODO("success scope")
     }
    */


}
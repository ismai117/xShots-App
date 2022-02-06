package com.im.xshots.ui.util

sealed class NetworkState<T>(
    val data: T? = null,
    val error: Throwable? = null
){

    class Success<T>(data: T?) : NetworkState<T>(data)
    class Error <T>(error: Throwable?) : NetworkState<T>(error = error)
    class Loading<T> () : NetworkState<T>()

}

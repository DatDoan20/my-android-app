package com.doanducdat.shoppingapp.utils

data class DataState<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): DataState<T> = DataState(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): DataState<T> =
            DataState(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): DataState<T> = DataState(status = Status.LOADING, data = data, message = null)
    }
}
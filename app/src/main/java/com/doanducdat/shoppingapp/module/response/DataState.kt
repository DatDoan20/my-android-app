package com.doanducdat.shoppingapp.module.response

data class DataState<out T>(val status: Status, val response: T?, val message: String?) {
    /***
     * DataState:
     * status: loading, success, error
     * data: -> status, data
     * message
     */
    companion object {
        fun <T> success(response: T): DataState<T> =
            DataState(status = Status.SUCCESS, response = response, message = null)

        fun <T> error(response: T?, message: String): DataState<T> =
            DataState(status = Status.ERROR, response = response, message = message)

        fun <T> loading(response: T?): DataState<T> =
            DataState(status = Status.LOADING, response = response, message = null)
    }
}
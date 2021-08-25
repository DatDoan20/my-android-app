package com.doanducdat.shoppingapp.utils.validation

import com.doanducdat.shoppingapp.utils.AppConstants
import retrofit2.HttpException
import java.io.IOException

object ResponseValidation {
    fun msgErrResponse(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                e.code().toString() + "\n" + e.response()?.errorBody()?.source()
            }
            is IOException -> {
                AppConstants.MsgError.GENERIC_ERR_RESPONSE
            }
            else -> AppConstants.MsgError.GENERIC_ERR_MSG
        }
    }
}
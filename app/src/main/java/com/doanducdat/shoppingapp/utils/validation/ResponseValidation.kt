package com.doanducdat.shoppingapp.utils.validation

import com.doanducdat.shoppingapp.module.response.ResponseAuth
import com.doanducdat.shoppingapp.module.response.ResponseUser
import com.doanducdat.shoppingapp.utils.AppConstants
import retrofit2.HttpException
import java.io.IOException
import com.google.gson.GsonBuilder


object ResponseValidation {
    fun msgErrResponse(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                // non-2xx
                e.code().toString() + "\n" + e.response()?.errorBody()?.source()
            }
            is IOException -> { //interrupted internet
                AppConstants.MsgErr.GENERIC_ERR_MSG
            }
            else -> e.message.toString()
        }
    }

    private fun convertErrResponse(e: Throwable, nameClass: Class<*>): Any? {
        if (e is HttpException) {
            val gson = GsonBuilder().create()
            return gson.fromJson(e.response()?.errorBody()?.string(), nameClass)
        }
        return null
    }

    fun errResponseUser(e: Throwable): ResponseUser? {
        return convertErrResponse(e, ResponseUser::class.java) as ResponseUser?
    }

    fun errResponseSignUp(e: Throwable): ResponseAuth? {
        return convertErrResponse(e, ResponseAuth::class.java) as ResponseAuth?
    }

    fun errResponseSignIn(e: Throwable): ResponseAuth? {
        return convertErrResponse(e, ResponseAuth::class.java) as ResponseAuth?
    }


}
package com.doanducdat.shoppingapp.utils.handler

import com.doanducdat.shoppingapp.model.response.ResponseAuth
import com.doanducdat.shoppingapp.model.response.ResponseError
import com.doanducdat.shoppingapp.model.response.ResponseUser
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException


object HandlerErrRes {
    fun errMsg(e: Throwable): String {
        return when (e) {
            is HttpException -> { // non-2xx
                e.code().toString() + "\n" + e.response()?.errorBody()?.source()
            }
            is IOException -> { //interrupted internet
                AppConstants.MsgErr.GENERIC_ERR_MSG
            }
            else -> e.message.toString()
        }
    }

    fun objectErr(e: Throwable): ResponseError {
        return convertErrResponse(e, ResponseError::class.java) as ResponseError
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

    fun checkMsg(msg: String?): String {
        return when {
            msg == null -> {
                AppConstants.MsgErr.GENERIC_ERR_MSG
            }
            msg.startsWith(AppConstants.Response.ERR_DUPLICATE, true) -> {
                AppConstants.MsgErr.MSG_ERR_DUPLICATE_SIGN_IN
            }
            msg.startsWith(AppConstants.Response.ERR_INCORRECT_PHONE_OR_PASS) -> {
                AppConstants.MsgErr.MSG_ERR_INCORRECT_PHONE_OR_PASS
            }
            msg.startsWith(AppConstants.Response.ERR_LIMIT, true) -> {
                AppConstants.MsgErr.LIMIT_ERR_MSG
            }
            msg == AppConstants.Response.ERR_JWT_EXPIRED -> {
                AppConstants.MsgErr.MSG_ERR_JWT_EXPIRED
            }
            // delegate server custom err msg
            msg.startsWith(AppConstants.Response.ERR_DELEGATE_SERVER) -> {
                msg
            }
            //default is err network or sth -> show generic
            else -> AppConstants.MsgErr.GENERIC_ERR_MSG
        }
    }
}
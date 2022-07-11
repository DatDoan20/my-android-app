package com.doanducdat.shoppingapp.utils.validation

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.util.Patterns
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.utils.AppConstants
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

/*** function check: error -> return "error message", not error -> return "null" */
object FormValidation {

    fun checkLengthNumberPhone(conditionResult: Boolean): String? {
        if (conditionResult) {
            return AppConstants.MsgErr.PHONE_ERR_LENGTH
        }
        return null
    }

    fun checkLengthName(conditionResult: Boolean): String? {
        if (conditionResult) {
            return AppConstants.MsgErr.NAME_ERR_LENGTH
        }
        return null
    }

    fun checkLengthPassword(conditionResult: Boolean): String? {
        if (conditionResult) {
            return AppConstants.MsgErr.PASSWORD_ERR_LENGTH
        }
        return null
    }

    fun checkValidationEmail(email: String): String? {
        if (Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            return null
        }
        return AppConstants.MsgErr.EMAIL_ERR_MSG
    }

    fun checkValidationNumberPhone(conditionResult: Boolean): Int {
        if (conditionResult) {
            return R.drawable.ic_valid
        }
        return R.drawable.ic_unvalid
    }

    fun checkValidationName(name: String): String? {
        val regexNumber = Pattern.compile(AppConstants.RegexCheck.CONTAINS_NUMBER);
        val regexSpecialCharacter =
            Pattern.compile(AppConstants.RegexCheck.CONTAINS_SPECIAL_CHARACTER);
        if (regexNumber.matcher(name).find() || regexSpecialCharacter.matcher(name).find()) {
            return AppConstants.MsgErr.NAME_ERR_MSG
        }
        return null
    }

    /*** return name was formatted */
    fun formatName(name: String): String {
        var formattedName: StringBuffer = StringBuffer()
        var list: List<String> = name.trim().split(" ")
        for (character in list) {
            if (character.trim().isNotEmpty()) {
                formattedName = formattedName
                    .append(character[0].uppercase())
                    .append(character.substring(1))
                    .append(" ")
            }
        }
        return formattedName.trim().toString()
    }

    fun formatMoney(number: Int): String {
        return DecimalFormat("#,###").format(number) + "đ"
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDay(date: Date, context: Context): String {
        val dateFormat = DateFormat.getMediumDateFormat(context)
        return dateFormat.format(date)
    }

    fun getContentStateOrder(totalPayment: Int, state: String): String? {
        val totalPaymentOrder = formatMoney(totalPayment)

        with(AppConstants.StateNotifyOrder) {
            return when (state) {
                AppConstants.Order.ACCEPTED -> {
                    "$MSG_ACCEPTED $totalPaymentOrder. $DETAIL_SUPPORT"
                }
                AppConstants.Order.RECEIVED -> {
                    "$MSG_RECEIVED $totalPaymentOrder. $MSG_THANKS \n$DETAIL_SUPPORT"
                }
                AppConstants.Order.CANCELED -> {
                    "Đơn hàng: $totalPaymentOrder $MSG_CANCELED. $DETAIL_SUPPORT"
                }
                else -> null
            }
        }
    }

    fun getContentComment(comment: String): String {
        return if (comment.trim().length > 53) "${comment.substring(0, 50)}..." else comment
    }

    fun getNameFirstProduct(order: Order, sizePurchasedProduct: Int): String {
        val nameFirstProduct = order.purchasedProducts[0].name
        //many product -> custom nameFirstProduct to display
        return if (sizePurchasedProduct > 1) {
            "${nameFirstProduct.substring(0, 20)} và $sizePurchasedProduct mặt hàng khác..."
        } else {
            nameFirstProduct
        }
    }
}
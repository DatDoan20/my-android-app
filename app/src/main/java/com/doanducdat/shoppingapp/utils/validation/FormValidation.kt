package com.doanducdat.shoppingapp.utils.validation

import android.util.Patterns
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.utils.AppConstants
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
        val name = name.trim()
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
}
package com.doanducdat.shoppingapp.utils

object HandlerTime {
    fun getTimeAgo(timeInput: Long): String? {
        var time = timeInput
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        val diff = now - time

        return when {
            diff < AppConstants.Time.MINUTE_MILLIS -> {
                "vừa tức thì"
            }
            diff < 2 * AppConstants.Time.MINUTE_MILLIS -> {
                "1 phút trước"
            }
            diff < 60 * AppConstants.Time.MINUTE_MILLIS -> {
                (diff / AppConstants.Time.MINUTE_MILLIS).toString() + " phút trước"
            }
            diff < 119 * AppConstants.Time.MINUTE_MILLIS -> {
                "1h" + ((diff - 60) / AppConstants.Time.MINUTE_MILLIS) + " phút trước"
            }
            diff < 24 * AppConstants.Time.HOUR_MILLIS -> {
                (diff / AppConstants.Time.HOUR_MILLIS).toString() + " h trước"
            }
            diff < 48 * AppConstants.Time.HOUR_MILLIS -> {
                "hôm qua"
            }
            else -> {
                (diff / AppConstants.Time.DAY_MILLIS).toString() + " ngày trước"
            }
        }
    }
}
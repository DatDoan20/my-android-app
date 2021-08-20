package com.doanducdat.shoppingapp.utils

import android.content.res.Configuration

class ThemeApp {
    companion object {
        /***
         * Light Theme -> return true, Night Theme -> return false
         */
        fun getCurrentTheme(): Boolean {
            if (Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
                return true
            }
            return false
        }
    }
}
package com.doanducdat.shoppingapp.utils.validation

import android.content.res.Configuration

object ThemeApp{
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
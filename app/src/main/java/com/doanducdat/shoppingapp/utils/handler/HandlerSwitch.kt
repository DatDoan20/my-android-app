package com.doanducdat.shoppingapp.utils.handler

import android.util.Log
import androidx.navigation.NavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.utils.AppConstants

object HandlerSwitch {

    fun navigationToFragment(
        idFragment: Int,
        controller: NavController,
        isOpened: Boolean? = null
    ) {
        if (idFragment == R.id.homeFragment) {
//            if (fragmentIsInBackStack(idFragment, controller)) {
//                Log.d("TEST", "navigationToFragment: ")
            controller.popBackStack(idFragment, false)
//            } else {
//                controller.navigate(idFragment)
//            }
        } else {
            controller.navigate(idFragment)
        }
    }

    private fun fragmentIsInBackStack(idFragment: Int, controller: NavController): Boolean {
        return try {
            controller.getBackStackEntry(idFragment)
            true
        } catch (e: Exception) {
            Log.e(AppConstants.TAG.BACK_FRAGMENT, "fragmentIsInBackStack: ${e.message}")
            Log.e(AppConstants.TAG.BACK_FRAGMENT, "fragmentIsInBackStack: ${e.printStackTrace()}")
            false
        }
    }


}
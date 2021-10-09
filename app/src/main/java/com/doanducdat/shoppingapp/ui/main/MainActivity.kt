package com.doanducdat.shoppingapp.ui.main

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityMainBinding
import com.doanducdat.shoppingapp.service.WebSocketIO
import com.doanducdat.shoppingapp.ui.base.BaseActivity
import com.doanducdat.shoppingapp.ui.main.notification.NotificationShareViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val controller by lazy {
        (supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private val webSocketIO by lazy { WebSocketIO() }
    private val notificationShareViewModel by lazy {
        ViewModelProvider(this).get(NotificationShareViewModel::class.java)
    }

    override fun getViewBinding(): Int = R.layout.activity_main

    override fun setUpView() {
        setUpActionClick()
        updateBadgeCountCart()

        //socket connect
        with(webSocketIO) {
            initSocket(this@MainActivity)
            getSocket()?.connect()
            emitSignIn()
            listenNewNotifyComment()
            listenStateNotifyOrder()
        }
    }

    private fun updateBadgeCountCart() {
        binding.bubbleBtmNvgMain.setBadgeValue(3, InfoUser.currentUser?.cart?.size.toString())
    }

    private fun setUpActionClick() {
        binding.bubbleBtmNvgMain.setNavigationChangeListener { _, position ->
            //navigate fragment when click
            when (position) {
                0 -> {
                    navigationToFragment(R.id.homeFragment)
                }
                1 -> {
                    navigationToFragment(R.id.categoryFragment)
                }
                2 -> {
                    navigationToFragment(R.id.searchFragment)
                }
                3 -> {
                    navigationToFragment(R.id.cartFragment)
                }
                4 -> {
                    navigationToFragment(R.id.profileFragment)
                }
            }
            updateBadgeCountCart()
        }
        //hide bottom navigation
        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
                    binding.bubbleBtmNvgMain.setCurrentActiveItem(2)
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.homeFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.categoryFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.profileFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.cartFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                else -> binding.bubbleBtmNvgMain.visibility = View.GONE
            }
            updateBadgeCountCart()
        }
    }

    private fun navigationToFragment(idFragment: Int) {
        if (fragmentIsInBackStack(idFragment)) {
            //this fragment is in backstack
            controller.popBackStack(idFragment, false)
        } else {
            controller.navigate(idFragment)
        }
    }

    private fun fragmentIsInBackStack(idFragment: Int): Boolean {
        return try {
            controller.getBackStackEntry(idFragment)
            true
        } catch (e: Exception) {
            Log.e(AppConstants.TAG.BACK_FRAGMENT, "fragmentIsInBackStack: ${e.message}")
            Log.e(AppConstants.TAG.BACK_FRAGMENT, "fragmentIsInBackStack: ${e.printStackTrace()}")
            false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        with(webSocketIO) {
            getSocket()?.disconnect()
            offListenNewNotifyComment()
            offListenStateNotifyOrder()
        }
    }
}
package com.doanducdat.shoppingapp.ui.main

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityMainBinding
import com.doanducdat.shoppingapp.service.WebSocketIO
import com.doanducdat.shoppingapp.ui.base.BaseActivity
import com.doanducdat.shoppingapp.ui.main.notification.NotificationShareViewModel
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.handler.HandlerSwitch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

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
        binding.bubbleBtmNvgMain.setBadgeValue(3, InfoLocalUser.currentUser?.cart?.size.toString())
    }

    private fun setUpActionClick() {
        binding.bubbleBtmNvgMain.setNavigationChangeListener { _, position ->
            //navigate fragment when click
            when (position) {
                0 -> {
                    HandlerSwitch.navigationToFragment(R.id.homeFragment, controllerMain)
                }
                1 -> {
                    HandlerSwitch.navigationToFragment(R.id.categoryFragment, controllerMain)
                }
                2 -> {
                    HandlerSwitch.navigationToFragment(R.id.searchFragment, controllerMain)
                }
                3 -> {
                    HandlerSwitch.navigationToFragment(R.id.cartFragment, controllerMain)
                }
                4 -> {
                    HandlerSwitch.navigationToFragment(R.id.profileFragment, controllerMain)
                }
            }
            updateBadgeCountCart()
        }
        //hide bottom navigation
        controllerMain.addOnDestinationChangedListener { _, destination, _ ->
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


    override fun onDestroy() {
        super.onDestroy()
        with(webSocketIO) {
            getSocket()?.disconnect()
            offListenNewNotifyComment()
            offListenStateNotifyOrder()
        }
    }
}
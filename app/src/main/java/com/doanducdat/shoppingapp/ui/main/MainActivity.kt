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

/*
* c1. use viewpager(1) to load fragment of list bottom navigation
* c2. use NavigationController(2) to load fragment into FragmentView(3) in
 MainActivity (so lag when load fragment on FragmentView-layout(4) )
 */

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
        val badge = binding.bubbleBtmNvgMain.getOrCreateBadge(3)
        badge.isVisible = true
        badge.number = InfoLocalUser.currentUser?.cart?.size ?: 0
    }

    private fun setUpActionClick() {
//        val arrFragment = ArrayList<Fragment>()
//        arrFragment.add(HomeFragment())
//        arrFragment.add(CategoryFragment())
//        arrFragment.add(SearchFragment())
//        arrFragment.add(CartFragment())
//        arrFragment.add(ProfileFragment())
//        val bottomNavigationAdapter = BtmViewPagerAdapter(this, arrFragment)
//        binding.vpMain.adapter = bottomNavigationAdapter
//        binding.vpMain.isUserInputEnabled = false
//        binding.vpMain.offscreenPageLimit = 5

        binding.bubbleBtmNvgMain.setOnItemSelectedListener { item ->
            updateBadgeCountCart()
            when (item.itemId) {
                R.id.menu_home -> {
                    HandlerSwitch.navigationToFragment(R.id.homeFragment, controllerMain)
                }
                R.id.menu_category -> {
                    HandlerSwitch.navigationToFragment(R.id.categoryFragment, controllerMain)
                }
                R.id.menu_search -> {
                    HandlerSwitch.navigationToFragment(R.id.searchFragment, controllerMain)
                }
                R.id.menu_cart -> {
                    HandlerSwitch.navigationToFragment(R.id.cartFragment, controllerMain)
                }
                R.id.menu_profile -> {
                    HandlerSwitch.navigationToFragment(R.id.profileFragment, controllerMain)
                }
            }
            true
        }
        //hide bottom navigation
        controllerMain.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
//                    binding.containerMain.visibility = View.GONE
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.homeFragment -> {
//                    binding.containerMain.visibility = View.GONE
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.categoryFragment -> {
//                    binding.containerMain.visibility = View.GONE
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.profileFragment -> {
//                    binding.containerMain.visibility = View.GONE
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.cartFragment -> {
//                    binding.containerMain.visibility = View.GONE
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                else -> {
//                    binding.containerMain.visibility = View.VISIBLE
                    binding.bubbleBtmNvgMain.visibility = View.GONE
                }
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
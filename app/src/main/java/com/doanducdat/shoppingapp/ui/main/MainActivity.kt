package com.doanducdat.shoppingapp.ui.main

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityMainBinding
import com.doanducdat.shoppingapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val controller by lazy {
        (supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    override fun getViewBinding(): Int = R.layout.activity_main

    override fun setUpView() {
        setUpActionClick()
    }

    private fun setUpActionClick() {
        binding.bubbleBtmNvgMain.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> {
                    controller.navigate(R.id.homeFragment)
                }
                1 -> {
                    controller.navigate(R.id.categoryFragment)
                }
                2 -> {
                    controller.navigate(R.id.searchFragment)
                }
                3 -> {
                    controller.navigate(R.id.cartFragment)
                }
                4 -> {
                    controller.navigate(R.id.profileFragment)
                }
            }
        }
    }
}
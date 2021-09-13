package com.doanducdat.shoppingapp.ui.main

import android.view.View
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
        }
        //hide bottom navigation
        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
                    binding.bubbleBtmNvgMain.setCurrentActiveItem(2)
                    binding.bubbleBtmNvgMain.visibility = View.VISIBLE
                }
                R.id.productListFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.GONE
                }
                R.id.productFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.GONE
                }
                R.id.productPhotoViewFragment -> {
                    binding.bubbleBtmNvgMain.visibility = View.GONE
                }
                else -> binding.bubbleBtmNvgMain.visibility = View.VISIBLE

            }
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
            false
        }
    }
}
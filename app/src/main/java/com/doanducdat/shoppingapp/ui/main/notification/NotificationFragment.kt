package com.doanducdat.shoppingapp.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.NotificationPagerAdapter
import com.doanducdat.shoppingapp.databinding.FragmentNotificationBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)


    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()
        setUpViewpagerNotification()
        setUpTabLayoutWithViewPager()
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun setUpViewpagerNotification() {
        binding.viewPagerNotification.adapter = NotificationPagerAdapter(requireActivity())
    }

    private fun setUpTabLayoutWithViewPager() {
        //set icon + text
        TabLayoutMediator(
            binding.tabLayoutNotification,
            binding.viewPagerNotification
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = AppConstants.TextTab.COMMENT
                    tab.icon = getMyDrawable(R.drawable.ic_comment_notification)
                    // set default when launch
                    changeColorTabIcon(tab, R.color.white)

                    val badge = tab.orCreateBadge
                    badge.number = 1
                }
                1 -> {
                    tab.text = AppConstants.TextTab.ORDER
                    tab.icon = getMyDrawable(R.drawable.ic_order_notification_blue)
                    changeColorTabIcon(tab, R.color.boxStrokeTextInputGeneric)
                }

            }
        }.attach()

        //listen change to set style ( tabLayoutMediator will be listen onTabSelectedListener of tabLayout)
        binding.tabLayoutNotification.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    changeColorTabIcon(tab, R.color.white)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    changeColorTabIcon(tab, R.color.boxStrokeTextInputGeneric)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

        })
    }
}
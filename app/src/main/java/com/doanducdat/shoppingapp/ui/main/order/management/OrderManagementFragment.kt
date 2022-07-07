package com.doanducdat.shoppingapp.ui.main.order.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doanducdat.shoppingapp.adapter.OrderManagementPagerAdapter
import com.doanducdat.shoppingapp.databinding.FragmentOrderManagementBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.android.material.tabs.TabLayoutMediator


class OrderManagementFragment : BaseFragment<FragmentOrderManagementBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderManagementBinding =
        FragmentOrderManagementBinding.inflate(inflater, container, false)

    val adapter by lazy { OrderManagementPagerAdapter(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(AppConstants.TAG.ORDER_MANAGEMENT, "viewCreated")

        setUpBackFragment()
        setUpViewpagerCategory()
        setUpTabLayoutWithViewPager()
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controllerMain.popBackStack()
        }
    }

    private fun setUpViewpagerCategory() {
        binding.viewPagerOrder.adapter = adapter
    }

    private fun setUpTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabLayoutManageOrder,
            binding.viewPagerOrder,
            true
        ) { tab, position ->
            when (position) {
                0 -> tab.text = AppConstants.TextTab.ALL_ORDER
                1 -> tab.text = AppConstants.TextTab.HANDING_ORDER
                2 -> tab.text = AppConstants.TextTab.ACCEPTED_ORDER
                3 -> tab.text = AppConstants.TextTab.RECEIVED_ORDER
                4 -> tab.text = AppConstants.TextTab.CANCELED_ORDER
            }
        }.attach()
    }

}
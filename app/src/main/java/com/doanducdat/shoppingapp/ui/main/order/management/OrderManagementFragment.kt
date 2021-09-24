package com.doanducdat.shoppingapp.ui.main.order.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
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

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
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
            controller.popBackStack()
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
                0 -> tab.text = "Tất cả"
                1 -> tab.text = "Đang chờ xử lý"
                2 -> tab.text = "Đặt hàng thành công"
                3 -> tab.text = "Giao hàng thành công"
                4 -> tab.text = "Đã hủy"
            }
        }.attach()
    }

}
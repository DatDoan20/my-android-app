package com.doanducdat.shoppingapp.ui.main.order.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doanducdat.shoppingapp.adapter.OrderManagementPagerAdapter
import com.doanducdat.shoppingapp.databinding.FragmentOrderManagementBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class OrderManagementFragment : BaseFragment<FragmentOrderManagementBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderManagementBinding =
        FragmentOrderManagementBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewpagerCategory()
        setUpTabLayoutWithViewPager()
    }
    private fun setUpViewpagerCategory() {
        binding.viewPagerOrder.adapter = OrderManagementPagerAdapter(requireActivity())
    }

    private fun setUpTabLayoutWithViewPager() {
        TabLayoutMediator(binding.tabLayoutManageOrder, binding.viewPagerOrder) { tab, position ->
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
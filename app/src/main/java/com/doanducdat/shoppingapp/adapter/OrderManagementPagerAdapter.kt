package com.doanducdat.shoppingapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doanducdat.shoppingapp.ui.main.order.management.MyOrdersFragment
import com.doanducdat.shoppingapp.ui.main.order.management.OrderHandlingFragment

class OrderManagementPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MyOrdersFragment()
            1 -> return OrderHandlingFragment()
            2 -> return OrderHandlingFragment()
            3 -> return OrderHandlingFragment()
            4 -> return OrderHandlingFragment()
        }
        return MyOrdersFragment()
    }
}
package com.doanducdat.shoppingapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doanducdat.shoppingapp.ui.onboard.fragment.FirstFragment
import com.doanducdat.shoppingapp.ui.onboard.fragment.SecondFragment
import com.doanducdat.shoppingapp.ui.onboard.fragment.ThirdFragment

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FirstFragment()
            1 -> return SecondFragment()
            2 -> return ThirdFragment()
        }
        return FirstFragment()
    }
}